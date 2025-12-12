package ru.bfofficial.lineshield.eco;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import ru.bfofficial.lineshield.eco.api.EconomyAPI;
import ru.bfofficial.lineshield.eco.commands.AddCoins;
import ru.bfofficial.lineshield.eco.commands.TakeCoins;
import ru.bfofficial.lineshield.eco.local.Localization;
import ru.bfofficial.lineshield.eco.placeholders.PlaceholderAP;
import ru.bfofficial.lineshield.eco.database.DatabaseManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.bfofficial.lineshield.eco.utils.ConfigManager;

import java.util.Objects;

public class Main extends JavaPlugin {

    private DatabaseManager databaseManager;
    private Localization localization;
    private static Main instance;
    public String vendor_name = "LineShield Team";
    String ver = getConfig().getString("config-version");

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Plugin core = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        if (core !=null && core.isEnabled()) {
            getLogger().info("Обнаружен PlaceholderAPI");
        }
        else {
            getLogger().warning("PlaceholderAPI не обнаружен. Плагин продолжит запуск, но некоторые функции могут работать неправильно...");
        }
        ConfigManager configManager = new ConfigManager(this);
        configManager.setup();

        localization = new Localization(this);
        localization.load();

        if (getConfig().getBoolean("splash-enable", true)) {
            getLogger().info("\n" +
                    "------------------------------------------------\n" +
                    "Загрузка конфигурации...\n" +
                    "  _     _   _   _____ _____    _    __  __\n" +
                    " | |   | \\ | | |_   _| ____|  / \\  |  \\/  |\n" +
                    " | |   |  \\| |   | | |  _|   / _ \\ | |\\/| |\n" +
                    " | |___| |\\  |   | | | |___ / ___ \\| |  | |\n" +
                    " |_____|_| \\_|   |_| |_____/_/   \\_\\_|  |_|\n" +
                    "\n" +
                    "Плагин разработан " + vendor_name + "\n" +
                    "Версия конфигурации: " + ver + "\n" +
                    "-----------------------------------------------");
        }

        new PlaceholderAP(this).register();

        databaseManager = new DatabaseManager(this);
        if (!databaseManager.initialize()) {
            getLogger().severe("Фатальная ошибка! Не удалось установить подключение к базе данных \n" +
                    "Проверьте конфигурацию и проверьте базу данных");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Objects.requireNonNull(getCommand("addcoins")).setExecutor(new AddCoins(this));
        Objects.requireNonNull(getCommand("takecoins")).setExecutor(new TakeCoins(this));
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) databaseManager.close();
    }

    public static Main getInstance() { return instance; }
    public DatabaseManager getDatabaseManager() { return databaseManager; }
    public Localization getLocalization() { return localization; }

    public EconomyAPI getEconomyAPI() {
        return new EconomyAPI(this);
    }
}