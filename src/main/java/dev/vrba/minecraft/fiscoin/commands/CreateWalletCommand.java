package dev.vrba.minecraft.fiscoin.commands;

import dev.vrba.minecraft.fiscoin.Fiscoin;
import dev.vrba.minecraft.fiscoin.WalletsManager;
import dev.vrba.minecraft.fiscoin.blockchain.FiscoinWallet;
import org.apache.commons.codec.binary.Hex;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class CreateWalletCommand implements CommandExecutor {

    private final Fiscoin plugin;

    public CreateWalletCommand(@NotNull Fiscoin plugin) {
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
            WalletsManager manager = plugin.getWalletsManager();

            String message = "Wallet generated";
            Optional<FiscoinWallet> potential = manager.walletOf(player);

            if (potential.isPresent()) {
                message = "Wallet loaded";
            }

            FiscoinWallet wallet = potential.orElseGet(() -> manager.generate(player));
            String publicKey = FiscoinWallet.fingerprint(wallet.getPublicKey());

            player.sendTitle(
                    message,
                    ChatColor.AQUA + publicKey + ChatColor.RESET,
                    20,
                    100,
                    20
            );
        }

        return false;
    }
}
