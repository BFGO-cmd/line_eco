package ru.bfofficial.lineshield.eco.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import ru.bfofficial.lineshield.eco.Main;
import ru.bfofficial.lineshield.eco.api.EconomyAPI;

public class PlaceholderAP extends PlaceholderExpansion {

    private final Main plugin;
    private final EconomyAPI api;

    public PlaceholderAP(Main plugin) {
        this.plugin = plugin;
        this.api = plugin.getEconomyAPI();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "lineco";
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null || player.getName() == null) {
            return "N/A";
        }

        String playerName = player.getName();

        if (params.equalsIgnoreCase("balance")) {
            return String.valueOf(api.getBalance(playerName));
        }

        if (params.equalsIgnoreCase("currency")) {
            return api.getCurrencyName();
        }

        if (params.equalsIgnoreCase("balance_formatted")) {
            int balance = api.getBalance(playerName);
            String currency = api.getCurrencyName();
            return balance + " " + currency;
        }

        return null;
    }
}