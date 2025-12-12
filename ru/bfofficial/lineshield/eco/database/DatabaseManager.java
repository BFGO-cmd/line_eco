package ru.bfofficial.lineshield.eco.database;

import org.bukkit.configuration.file.FileConfiguration;
import ru.bfofficial.lineshield.eco.Main;

public class DatabaseManager {
    private final Main plugin;
    private HikariDatabase database;
    private DatabaseType type;

    public DatabaseManager(Main plugin) {
        this.plugin = plugin;
    }

    public boolean initialize() {
        FileConfiguration cfg = plugin.getConfig();
        String typeStr = cfg.getString("database-type", "SQLITE").toUpperCase();
        try {
            this.type = DatabaseType.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().severe("Invalid database type: " + typeStr);
            return false;
        }

        database = new HikariDatabase(plugin, type);
        return database.connect();
    }

    public HikariDatabase getDatabase() {
        return database;
    }

    public void close() {
        if (database != null) database.close();
    }
}