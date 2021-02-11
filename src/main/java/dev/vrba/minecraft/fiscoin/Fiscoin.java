package dev.vrba.minecraft.fiscoin;

import dev.vrba.minecraft.fiscoin.commands.CreateWalletCommand;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

@SuppressWarnings("unused")
public final class Fiscoin extends JavaPlugin {

    @Override
    public void onEnable() {
        registerCommands();
        registerEventListeners();
    }

    @Override
    public void onDisable() {
    }

    private void registerCommands() {
        Map<String, ? extends CommandExecutor> commands = Map.of(
               "create-wallet", new CreateWalletCommand()
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
        // ...
    }
}
