package ru.bfofficial.lineshield.eco.commands;

import ru.bfofficial.lineshield.eco.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.bfofficial.lineshield.eco.local.MessageKey;

public class AddCoins implements CommandExecutor {

    private final Main plugin;

    public AddCoins(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("economy.addcoins")) {
            sender.sendMessage(plugin.getLocalization().getMessage(MessageKey.NO_PERMISSION));
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage("Usage: /addcoins <player> <amount>");
            return true;
        }

        String targetName = args[0];
        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(plugin.getLocalization().getMessage(MessageKey.INVALID_AMOUNT));
            return true;
        }

        if (amount <= 0) {
            sender.sendMessage(plugin.getLocalization().getMessage(MessageKey.INVALID_AMOUNT));
            return true;
        }

        if (Bukkit.getPlayer(targetName) == null && !Bukkit.getOfflinePlayer(targetName).hasPlayedBefore()) {
            sender.sendMessage(plugin.getLocalization().getMessage(MessageKey.PLAYER_NOT_FOUND, "{player}", targetName));
            return true;
        }

        plugin.getEconomyAPI().addBalance(targetName, amount);

        String msg = plugin.getLocalization().getMessage(MessageKey.ADDCOINS_SUCCESS,
                "{player}", targetName,
                "{amount}", String.valueOf(amount)
        );
        sender.sendMessage(msg);

        Player target = Bukkit.getPlayer(targetName);
        if (target != null) {
            target.sendMessage(msg.replace("Добавлено", "Вам добавлено"));
        }

        return true;
    }
}