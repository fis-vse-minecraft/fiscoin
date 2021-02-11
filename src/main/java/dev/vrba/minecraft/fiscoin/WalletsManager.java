package dev.vrba.minecraft.fiscoin;

import dev.vrba.minecraft.fiscoin.blockchain.Wallet;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Just a small note, that this class completely destroys the decentralization purpose
 * This literally stores private keys of all users. Big yikes, but this plugin was made just for fun...
 */
public class WalletsManager {

    private final Map<UUID, Wallet> wallets = new HashMap<>();

    public @NotNull Wallet generate(@NotNull Player player) {
        // A new wallet with random private and public key pair
        return Objects.requireNonNull(
            this.wallets.put(
                player.getUniqueId(),
                new Wallet()
            )
        );
    }

    public @NotNull Optional<Wallet> walletOf(@NotNull Player player) {
        return Optional.of(this.wallets.get(player.getUniqueId()));
    }
}
