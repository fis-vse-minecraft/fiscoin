package dev.vrba.minecraft.fiscoin.listeners;

import dev.vrba.minecraft.fiscoin.Fiscoin;
import dev.vrba.minecraft.fiscoin.blockchain.FiscoinBlock;
import dev.vrba.minecraft.fiscoin.blockchain.FiscoinBlockchain;
import dev.vrba.minecraft.fiscoin.blockchain.FiscoinTransaction;
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
        if (plugin.getPendingTransactions().isEmpty()) {
            return;
        }

        FiscoinBlockchain blockchain = plugin.getBlockchain();
        FiscoinTransaction transaction = plugin.getPendingTransactions().get(0);

        FiscoinBlock block = new FiscoinBlock(blockchain.getLastBlockHash(), transaction.toString(), "");

        // TODO: Calculate number of guesses based on mined block type

    }
}
