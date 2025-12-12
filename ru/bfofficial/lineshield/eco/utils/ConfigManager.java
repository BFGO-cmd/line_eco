package ru.bfofficial.lineshield.eco.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import ru.bfofficial.lineshield.eco.Main;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConfigManager {

    private final Main plugin;

    public ConfigManager(Main plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }

        plugin.reloadConfig();
        YamlConfiguration config = (YamlConfiguration) plugin.getConfig();
        InputStream defConfigStream = plugin.getResource("config.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
            config.setDefaults(defConfig);
            config.options().copyDefaults(true);
            try {
                config.save(configFile);
            } catch (Exception e) {
                plugin.getLogger().warning("Не удалось сохранить config.yml");
            }
        }
    }
}