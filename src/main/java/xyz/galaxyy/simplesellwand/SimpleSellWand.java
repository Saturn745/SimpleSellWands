package xyz.galaxyy.simplesellwand;

import org.bukkit.plugin.java.JavaPlugin;

import xyz.galaxyy.simplesellwand.commands.ReloadConfigCommand;
import xyz.galaxyy.simplesellwand.commands.SetWandCommand;
import xyz.galaxyy.simplesellwand.config.ConfigManager;
import xyz.galaxyy.simplesellwand.config.CoreConfig;
import xyz.galaxyy.simplesellwand.hooks.Hooks;
import xyz.galaxyy.simplesellwand.listeners.PlayerInteractListener;

public final class SimpleSellWand extends JavaPlugin {
    private static SimpleSellWand instance;
    private final ConfigManager<CoreConfig> configManager = ConfigManager.create(this.getDataFolder().toPath(),
            "config.conf", CoreConfig.class);

    @Override
    public void onEnable() {
        instance = this;
        this.configManager.reloadConfig();
        Hooks.init();
        this.getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        this.getServer().getCommandMap().register("simplesellwand", new ReloadConfigCommand());
        this.getServer().getCommandMap().register("simplesellwand", new SetWandCommand());
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public ConfigManager<CoreConfig> getConfigManager() {
        return configManager;
    }

    public static SimpleSellWand getInstance() {
        return instance;
    }
}
