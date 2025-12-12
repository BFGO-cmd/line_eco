package ru.bfofficial.lineshield.eco.local;

public enum MessageKey {
    NO_PERMISSION("command.no-permission"),
    INVALID_AMOUNT("command.invalid-amount"),
    PLAYER_NOT_FOUND("command.player-not-found"),
    ADDCOINS_SUCCESS("addcoins.success"),
    TAKECOINS_SUCCESS("takecoins.success"),
    TAKECOINS_INSUFFICIENT("takecoins.insufficient-funds"),
    CURRENCY_NAME("api.currency-name");

    private final String key;
    MessageKey(String key) { this.key = key; }
    public String getKey() { return key; }
}
