package dev.vrba.minecraft.fiscoin;

import dev.vrba.minecraft.fiscoin.blockchain.FiscoinBlock;
import dev.vrba.minecraft.fiscoin.blockchain.FiscoinBlockchain;
import dev.vrba.minecraft.fiscoin.commands.CreateWalletCommand;
import dev.vrba.minecraft.fiscoin.commands.ViewBlockchainCommand;
import dev.vrba.minecraft.fiscoin.listeners.MiningEventListener;
import lombok.Getter;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

@SuppressWarnings("unused")
public final class Fiscoin extends JavaPlugin {

    public static final String SYMBOL = "𝔽";

    // How many leading zeros does the block hash need to be considered a valid proof-of-work
    public static final int MINING_DIFFICULTY = 2;

    @Getter
    private final WalletsManager walletsManager = new WalletsManager();

    // Ideally this blockchain would be managed by each player, and there would be a false reject mechanism,
    // For now, this will be implemented as center blockchain, that requires it's modification to be valid
    @Getter
    private final FiscoinBlockchain blockchain = new FiscoinBlockchain();

    @Override
    public void onEnable() {
        registerCommands();
        registerEventListeners();

        // TODO: Run this in an async bukkit task
        calculateInitialBlock();
    }

    @Override
    public void onDisable() {
    }

    private void registerCommands() {
        Map<String, ? extends CommandExecutor> commands = Map.of(
                "wallet", new CreateWalletCommand(this),
                "blockchain", new ViewBlockchainCommand(this)
        );

        commands.forEach((name, executor) -> {
            PluginCommand command = getCommand(name);

            if (command == null) {
                throw new IllegalStateException("Cannot find command " + name);
            }

            command.setExecutor(executor);
        });
    }

    private void registerEventListeners() {
        PluginManager manager = Bukkit.getPluginManager();
        Listener[] listeners = new Listener[] {
            new MiningEventListener(this)
        };

        for (Listener listener : listeners) {
            manager.registerEvents(listener, this);
        }
    }

    private void calculateInitialBlock() {
        String hash = "";
        FiscoinBlock block = new FiscoinBlock(FiscoinBlock.INITIAL_HASH, "", "");
        String requiredProof = new String(new char[MINING_DIFFICULTY]).replace('\0', '0');

        while (!hash.startsWith(requiredProof)) {
            String nonce = RandomStringUtils.randomAlphanumeric(8);

            block = new FiscoinBlock(FiscoinBlock.INITIAL_HASH, "", nonce);
            hash = block.getHash();
        }

        System.out.println("Created first fiscoin blockchain block [" + hash + "]");

        blockchain.add(block);
    }
}
