package dev.vrba.minecraft.fiscoin.commands;

import dev.vrba.minecraft.fiscoin.Fiscoin;
import dev.vrba.minecraft.fiscoin.blockchain.FiscoinBlock;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

public class ViewBlockchainCommand implements CommandExecutor {

    private final Fiscoin plugin;

    public ViewBlockchainCommand(Fiscoin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
        @NotNull CommandSender sender,
        @NotNull Command command,
        @NotNull String label,
        @NotNull String[] args
    ) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String entries = plugin.getBlockchain().getBlocks()
                    .stream()
                    .map(this::blockToString)
                    .collect(Collectors.joining("\n"));

            player.sendMessage(entries);

            return true;
        }

        return false;
    }

    @NotNull
    private String blockToString(@NotNull FiscoinBlock block) {
        return new StringBuilder()
                .append(ChatColor.GRAY)
                .append(StringUtils.abbreviate(block.getPreviousHash(), 16))
                .append("...")
                .append(ChatColor.RESET)
                .append(" -> ")
                .append(ChatColor.GREEN)
                .append(StringUtils.abbreviate(block.getHash(), 16))
                .append(ChatColor.RESET)
                .toString();

    }
}
