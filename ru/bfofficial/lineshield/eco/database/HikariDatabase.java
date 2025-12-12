package ru.bfofficial.lineshield.eco.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.bfofficial.lineshield.eco.Main;

import java.sql.*;

public class HikariDatabase {
    private final Main plugin;
    private final DatabaseType type;
    private HikariDataSource dataSource;

    public HikariDatabase(Main plugin, DatabaseType type) {
        this.plugin = plugin;
        this.type = type;
    }

    public boolean connect() {
        HikariConfig config = new HikariConfig();
        config.setLeakDetectionThreshold(10_000);

        if (type == DatabaseType.SQLITE) {
            String dbFile = plugin.getDataFolder().toPath().resolve("data.db").toString();
            config.setJdbcUrl("jdbc:sqlite:" + dbFile);
            config.setMaximumPoolSize(1);
        } else {
            var cfg = plugin.getConfig().getConfigurationSection("mysql");
            String url = String.format("jdbc:mysql://%s:%d/%s?useSSL=false&allowPublicKeyRetrieval=true",
                    cfg.getString("host"), cfg.getInt("port"), cfg.getString("database"));
            config.setJdbcUrl(url);
            config.setUsername(cfg.getString("username"));
            config.setPassword(cfg.getString("password"));
            config.setMaximumPoolSize(cfg.getInt("pool-size", 10));
        }

        try {
            dataSource = new HikariDataSource(config);
            createTables();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void createTables() {
        String sql = """
            CREATE TABLE IF NOT EXISTS balances (
                player_name VARCHAR(36) PRIMARY KEY,
                bal INT NOT NULL DEFAULT 0
            )
            """;
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to create table: " + e.getMessage());
        }
    }

    public int getBalance(String playerName) {
        String sql = "SELECT bal FROM balances WHERE player_name = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, playerName);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("bal") : 0;
        } catch (SQLException e) {
            plugin.getLogger().warning("Failed to get balance for " + playerName + ": " + e.getMessage());
            return 0;
        }
    }

    public void setBalance(String playerName, int balance) {
        String sql = """
            INSERT INTO balances (player_name, bal) VALUES (?, ?)
            ON DUPLICATE KEY UPDATE bal = ?
            """;
        if (type == DatabaseType.SQLITE) {
            sql = """
                INSERT INTO balances (player_name, bal) VALUES (?, ?)
                ON CONFLICT(player_name) DO UPDATE SET bal = ?
                """;
        }
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, playerName);
            stmt.setInt(2, balance);
            stmt.setInt(3, balance);
            stmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().warning("Failed to set balance for " + playerName + ": " + e.getMessage());
        }
    }

    public void addBalance(String playerName, int amount) {
        setBalance(playerName, getBalance(playerName) + amount);
    }

    public boolean withdrawBalance(String playerName, int amount) {
        int current = getBalance(playerName);
        if (current >= amount) {
            setBalance(playerName, current - amount);
            return true;
        }
        return false;
    }

    public void close() {
        if (dataSource != null) dataSource.close();
    }
}
