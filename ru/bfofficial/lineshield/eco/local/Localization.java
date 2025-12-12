package ru.bfofficial.lineshield.eco.local;

import org.bukkit.configuration.file.YamlConfiguration;
import ru.bfofficial.lineshield.eco.Main;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class Localization {
    private final Main plugin;
    private YamlConfiguration localeFile;

    public Localization(Main plugin) {
        this.plugin = plugin;
    }

    public void load() {
        String locale = plugin.getConfig().getString("locale", "en_US");
        File localeFolder = new File(plugin.getDataFolder(), "localizations");
        File localeFile = new File(localeFolder, locale + ".yml");

        if (!localeFile.exists()) {
            localeFolder.mkdirs();
            plugin.saveResource("localizations/ru_RU.yml", false);
            plugin.getLogger().info("Создан файл локализации: ru_RU.yml");
        }

        localeFile = new File(localeFolder, locale + ".yml");
        this.localeFile = YamlConfiguration.loadConfiguration(localeFile);

        InputStream defaultStream = plugin.getResource("localizations/" + locale + ".yml");
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.localeFile.setDefaults(defaultConfig);
            this.localeFile.options().copyDefaults(true);
            try {
                this.localeFile.save(localeFile);
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Не удалось сохранить файл локализации", e);
            }
        }
    }

    public String getMessage(MessageKey key, String... replacements) {
        String msg = localeFile.getString(key.getKey(), "Missing translation: " + key.getKey());
        msg = msg.replace("{currency}", getCurrencyName());
        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                msg = msg.replace(replacements[i], replacements[i + 1]);
            }
        }
        return msg;
    }

    public String getCurrencyName() {
        return getMessage(MessageKey.CURRENCY_NAME);
    }
}
