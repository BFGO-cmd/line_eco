package ru.bfofficial.lineshield.eco.api;

import ru.bfofficial.lineshield.eco.Main;
import ru.bfofficial.lineshield.eco.local.MessageKey;

public class EconomyAPI {

    private final Main plugin;

    public EconomyAPI(Main plugin) {
        this.plugin = plugin;
    }

    public boolean hasBalance(String playerName, int amount) {
        return plugin.getDatabaseManager().getDatabase().getBalance(playerName) >= amount;
    }

    public int getBalance(String playerName) {
        return plugin.getDatabaseManager().getDatabase().getBalance(playerName);
    }

    public void setBalance(String playerName, int amount) {
        plugin.getDatabaseManager().getDatabase().setBalance(playerName, amount);
    }

    public void addBalance(String playerName, int amount) {
        plugin.getDatabaseManager().getDatabase().addBalance(playerName, amount);
    }

    public boolean withdrawBalance(String playerName, int amount) {
        return plugin.getDatabaseManager().getDatabase().withdrawBalance(playerName, amount);
    }

    public String getCurrencyName() {
        return plugin.getLocalization().getMessage(MessageKey.valueOf("api.currency-name"));
    }
}
