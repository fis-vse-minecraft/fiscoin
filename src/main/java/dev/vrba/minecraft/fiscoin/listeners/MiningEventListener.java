package dev.vrba.minecraft.fiscoin.listeners;

import dev.vrba.minecraft.fiscoin.Fiscoin;
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
        event.getPlayer().sendMessage("Mined block... acquiring nonce guess.");
    }
}
