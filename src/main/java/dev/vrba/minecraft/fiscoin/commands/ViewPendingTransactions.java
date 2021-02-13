package dev.vrba.minecraft.fiscoin.commands;

import dev.vrba.minecraft.fiscoin.Fiscoin;
import dev.vrba.minecraft.fiscoin.blockchain.FiscoinTransaction;
import dev.vrba.minecraft.fiscoin.blockchain.FiscoinWallet;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.stream.Collectors;

public class ViewPendingTransactions implements CommandExecutor {

    private final Fiscoin plugin;

    public ViewPendingTransactions(@NotNull Fiscoin plugin) {
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

        if (plugin.getPendingTransactions().isEmpty()) {
            sender.sendMessage("There are currently no pending transactions");
            return true;
        }

        String transactions = plugin.getPendingTransactions()
                .stream()
                .map(this::printTransaction)
                .collect(Collectors.joining("\n"));

        player.sendMessage(transactions);
        return true;
    }

    @NotNull
    private String printTransaction(@NotNull FiscoinTransaction transaction) {
        return ChatColor.YELLOW + transaction.getId().toString() + ChatColor.RESET + "\n"
                + "From: " + FiscoinWallet.fingerprint(transaction.getSender()) + "\n"
                + "To: " + FiscoinWallet.fingerprint(transaction.getReceiver()) + "\n"
                + "Value: " + transaction.getValue() + Fiscoin.SYMBOL + "\n"
                + "-------------";
    }
}
