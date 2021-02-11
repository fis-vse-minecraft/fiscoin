package dev.vrba.minecraft.fiscoin;

import dev.vrba.minecraft.fiscoin.blockchain.FiscoinWallet;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Just a small note, that this class completely destroys the decentralization purpose
 * This literally stores private keys of all users. Big yikes, but this plugin was made just for fun...
 */
public class WalletsManager {

    private final Map<UUID, FiscoinWallet> wallets = new HashMap<>();

    public @NotNull FiscoinWallet generate(@NotNull Player player) {
        // A new wallet with random private and public key pair
        FiscoinWallet wallet = new FiscoinWallet();

        this.wallets.put(player.getUniqueId(), new FiscoinWallet());

        return wallet;
    }

    public @NotNull Optional<FiscoinWallet> walletOf(@NotNull Player player) {
        return Optional.ofNullable(this.wallets.get(player.getUniqueId()));
    }
}
