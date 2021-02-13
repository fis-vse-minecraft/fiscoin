package dev.vrba.minecraft.fiscoin.commands;

import dev.vrba.minecraft.fiscoin.Fiscoin;
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
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args
    ) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;
        Optional<FiscoinWallet> wallet = plugin.getWalletsManager().walletOf(player);

        if (wallet.isEmpty()) {
            player.sendMessage(
                ChatColor.RED + "You don't own a wallet, so you cannot create transaction signed with a private key" + ChatColor.RESET + "\n" +
                "You can generate a wallet by using " + ChatColor.YELLOW + "/wallet" + ChatColor.RESET + " command."
            );
            return false;
        }

        // TODO: Search for the public key of target player and create a singed pending transaction

        return true;
    }
}
