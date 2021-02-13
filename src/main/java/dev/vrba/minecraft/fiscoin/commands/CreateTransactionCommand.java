package dev.vrba.minecraft.fiscoin.commands;

import dev.vrba.minecraft.fiscoin.Fiscoin;
import dev.vrba.minecraft.fiscoin.WalletsManager;
import dev.vrba.minecraft.fiscoin.blockchain.FiscoinTransaction;
import dev.vrba.minecraft.fiscoin.blockchain.FiscoinWallet;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class CreateTransactionCommand implements CommandExecutor {

    private final Fiscoin plugin;

    public CreateTransactionCommand(@NotNull Fiscoin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender commandSender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args
    ) {
        if (!(commandSender instanceof Player)) {
            return false;
        }

        WalletsManager walletsManager = plugin.getWalletsManager();

        Player player = (Player) commandSender;
        Optional<FiscoinWallet> sender = walletsManager.walletOf(player);

        if (sender.isEmpty()) {
            player.sendMessage(
                    ChatColor.RED + "You don't own a wallet, so you cannot create transaction signed with a private key" + ChatColor.RESET + "\n" +
                            "You can generate a wallet by using " + ChatColor.YELLOW + "/wallet" + ChatColor.RESET + " command."
            );
            return false;
        }

        if (args.length != 2) {
            return false;
        }

        try {
            double value = Double.parseDouble(args[1]);
            assert value > 0.0;

            Optional<Player> target = ((Player) commandSender).getWorld()
                    .getPlayers()
                    .stream()
                    .filter(it -> it.getName().equals(args[0]))
                    .findFirst();

            if (target.isEmpty()) {
                player.sendMessage("Unknown player " + args[0]);
                return false;
            }

            Optional<FiscoinWallet> receiver = walletsManager.walletOf(target.get());

            if (receiver.isEmpty()) {
                player.sendMessage(ChatColor.RED + target.get().getName() + " doesn't have a wallet." + ChatColor.RESET);
                return false;
            }

            FiscoinTransaction transaction = new FiscoinTransaction(
                    sender.get().getPublicKey(),
                    receiver.get().getPublicKey(),
                    value
            )
            .sign(sender.get().getPrivateKey());

            plugin.addTransaction(transaction);

            player.getWorld()
                .getPlayers()
                .forEach(it -> {
                    it.sendMessage(
                        player.getDisplayName() + " created a new transaction " +
                        ChatColor.YELLOW + transaction.getId().toString() + ChatColor.RESET
                    );
                });

            return true;
        }
        catch (NumberFormatException | AssertionError exception) {
            player.sendMessage("Invalid transaction value. Expected a decimal value.");
            return false;
        }
    }
}
