package dev.vrba.minecraft.fiscoin;

import dev.vrba.minecraft.fiscoin.blockchain.FiscoinBlock;
import dev.vrba.minecraft.fiscoin.blockchain.FiscoinBlockchain;
import dev.vrba.minecraft.fiscoin.blockchain.FiscoinTransaction;
import dev.vrba.minecraft.fiscoin.blockchain.FiscoinWallet;
import dev.vrba.minecraft.fiscoin.commands.CreateTransactionCommand;
import dev.vrba.minecraft.fiscoin.commands.CreateWalletCommand;
import dev.vrba.minecraft.fiscoin.commands.ViewBlockchainCommand;
import dev.vrba.minecraft.fiscoin.commands.ViewPendingTransactions;
import dev.vrba.minecraft.fiscoin.listeners.MiningEventListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public final class Fiscoin extends JavaPlugin {

    public static final String SYMBOL = "Ƒ";

    // How many leading zeros does the block hash need to be considered a valid proof-of-work
    public static final int MINING_DIFFICULTY = 2;

    // Number of fiscoin obtained for every block validation
    public static final int MINING_REWARD = 5;

    @Getter
    private final WalletsManager walletsManager = new WalletsManager();

    // Ideally this blockchain would be managed by each player, and there would be a false reject mechanism,
    // For now, this will be implemented as center blockchain, that requires it's modification to be valid
    @Getter
    private final FiscoinBlockchain blockchain = new FiscoinBlockchain();

    // Also, this is one of the implementation pain points, as each miner would actually keep track of awaiting
    // transactions themselves and decide based on miner fee?
    @Getter
    private final List<FiscoinTransaction> pendingTransactions = new ArrayList<>();

    @Getter
    private FiscoinBlock currentTransactionBlock = null;

    @Override
    public void onEnable() {
        registerCommands();
        registerEventListeners();

        createInitialTransaction();
    }

    @Override
    public void onDisable() {
    }

    private void registerCommands() {
        Map<String, ? extends CommandExecutor> commands = Map.of(
                "wallet", new CreateWalletCommand(this),
                "blockchain", new ViewBlockchainCommand(this),
                "transactions", new ViewPendingTransactions(this),
                "send", new CreateTransactionCommand(this)
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
        Listener[] listeners = new Listener[]{
                new MiningEventListener(this)
        };

        for (Listener listener : listeners) {
            manager.registerEvents(listener, this);
        }
    }

    public void addTransaction(@NotNull FiscoinTransaction transaction) {
        pendingTransactions.add(transaction);

        if (currentTransactionBlock == null) {
            currentTransactionBlock = new FiscoinBlock(
                    blockchain.getLastBlockHash(),
                    pendingTransactions.get(0),
                    ""
            );
        }
    }

    public void addCurrentBlockSolution(@NotNull String nonce, @NotNull Player player) {
        if (currentTransactionBlock == null) {
            return;
        }

        ArrayList<FiscoinBlock> blocks = new ArrayList<>();
        Collections.copy(blockchain.getBlocks(), blocks);

        FiscoinBlockchain copy = new FiscoinBlockchain(blocks);
        FiscoinBlock solution = new FiscoinBlock(
                currentTransactionBlock.getPreviousHash(),
                currentTransactionBlock.getTransaction(),
                nonce,
                currentTransactionBlock.getTimestamp()
        );

        // TODO: This is also bad, because one fake block would invalidate the global blockchain...
        if (copy.add(solution).isValid()) {
            blockchain.add(solution);

            player.getWorld()
                    .getPlayers()
                    .forEach(target -> target.sendMessage(
                            player.getDisplayName() + " found nonce " + ChatColor.AQUA + nonce + ChatColor.RESET +
                                    " which validated transaction " + ChatColor.YELLOW + solution.getTransaction().getId() + ChatColor.RESET
                    ));

            pendingTransactions.remove(0);
            currentTransactionBlock = null;

            if (!this.pendingTransactions.isEmpty()) {
                currentTransactionBlock = new FiscoinBlock(
                        blockchain.getLastBlockHash(),
                        pendingTransactions.get(0),
                        ""
                );
            }
        }
    }

    private void createInitialTransaction() {
        FiscoinWallet alice = new FiscoinWallet();
        FiscoinWallet bob = new FiscoinWallet();

        FiscoinTransaction transaction = new FiscoinTransaction(
                alice.getPublicKey(),
                bob.getPublicKey(),
                0
        )
                .sign(alice.getPrivateKey());

        this.addTransaction(transaction);
    }
}
