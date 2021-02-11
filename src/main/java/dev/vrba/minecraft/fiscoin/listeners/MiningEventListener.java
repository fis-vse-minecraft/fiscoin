package dev.vrba.minecraft.fiscoin.listeners;

import dev.vrba.minecraft.fiscoin.Fiscoin;
import dev.vrba.minecraft.fiscoin.blockchain.FiscoinBlock;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

public class MiningEventListener implements Listener {

    private final Fiscoin plugin;

    public MiningEventListener(@NotNull Fiscoin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockMined(BlockBreakEvent event) {
        if (plugin.getCurrentTransactionBlock() == null) {
            return;
        }

        // TODO: Calculate number of guesses based on mined block type
        guess(event);
    }

    private void guess(@NotNull BlockBreakEvent event) {
        Player player = event.getPlayer();
        FiscoinBlock current = plugin.getCurrentTransactionBlock();

        String nonce = RandomStringUtils.randomAlphanumeric(16);
        FiscoinBlock guess = new FiscoinBlock(
                current.getPreviousHash(),
                current.getTransaction(),
                nonce,
                current.getTimestamp()
        );

        if (guess.hasValidHash()) {
            player.sendTitle(
                    "Hash found",
                    "You just earned " + Fiscoin.MINING_REWARD + " " + Fiscoin.SYMBOL,
                    10,
                    60,
                    10
            );

            plugin.addCurrentBlockSolution(nonce, player);
        }
        else {
            Location location = event.getBlock().getLocation();
            location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 25);
        }
    }
}
