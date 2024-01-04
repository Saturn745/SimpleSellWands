package xyz.galaxyy.simplesellwand.config;

import space.arim.dazzleconf.ConfigurationFactory;
import space.arim.dazzleconf.ConfigurationOptions;
import space.arim.dazzleconf.error.InvalidConfigException;
import space.arim.dazzleconf.ext.hocon.HoconConfigurationFactory;
import space.arim.dazzleconf.ext.hocon.HoconOptions;
import space.arim.dazzleconf.helper.ConfigurationHelper;
import xyz.galaxyy.simplesellwand.SimpleSellWand;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public final class ConfigManager<C> {

    private final ConfigurationHelper<C> configHelper;
    private volatile C configData;

    private ConfigManager(ConfigurationHelper<C> configHelper) {
        this.configHelper = configHelper;
    }

    public static <C> ConfigManager<C> create(Path configFolder, String fileName, Class<C> configClass) {
        HoconOptions hoconOptions = new HoconOptions.Builder().charset(StandardCharsets.UTF_8).build();
        ConfigurationFactory<C> configFactory = HoconConfigurationFactory.create(
                configClass,
                ConfigurationOptions.defaults(), // change this if desired
                hoconOptions);
        return new ConfigManager<>(new ConfigurationHelper<>(configFolder, fileName, configFactory));
    }

    public void reloadConfig() {
        try {
            configData = configHelper.reloadConfigData();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (InvalidConfigException e) {
            SimpleSellWand.getInstance().getLogger().severe("Your configuration file is not valid. " +
                    "Check to make sure you have not made any syntax errors.");
            e.printStackTrace();
        }
    }

    public C getConfigData() {
        C configData = this.configData;
        if (configData == null) {
            throw new IllegalStateException("Configuration has not been loaded yet");
        }
        return configData;
    }

}
