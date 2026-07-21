package me.xydesu.stoptimer.Commands;

import me.xydesu.stoptimer.Manager.ConfigManager;
import me.xydesu.stoptimer.Manager.Manager;
import me.xydesu.stoptimer.Manager.MessageManager;
import me.xydesu.stoptimer.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StopServer implements CommandExecutor, TabCompleter {

    private final Manager manager;
    private final MessageManager messages;
    private final ConfigManager configManager;

    public StopServer(Manager manager, MessageManager messages, ConfigManager configManager) {
        this.manager = manager;
        this.messages = messages;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // Permission check
        if (!(sender.hasPermission("stoptimer.stopserver") || sender.isOp())) {
            sender.sendMessage(messages.getNoPermission());
            return true;
        }

        // Reload config
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            FileConfiguration langConfig = Main.getInstance().reloadLangConfig();
            messages.reload(langConfig);
            configManager.reload(Main.getInstance().getConfig());
            sender.sendMessage(messages.getReload());
            return true;
        }

        // Cancel countdown
        if (args.length == 1 && args[0].equalsIgnoreCase("cancel")) {
            if (manager.cancelCountdown()) {
                sender.sendMessage(messages.getCanceled());
            } else {
                sender.sendMessage(messages.getCancelFail());
            }
            return true;
        }

        String timeArg = configManager.getDefaultTime();
        int reasonStartIndex = -1;

        if (args.length > 0) {
            if (args[0].startsWith("r:") || args[0].startsWith("t:")) {
                reasonStartIndex = 0;
            } else {
                timeArg = args[0];
                if (args.length > 1) {
                    if (args[1].startsWith("r:") || args[1].startsWith("t:")) {
                        reasonStartIndex = 1;
                    } else {
                        sender.sendMessage(messages.getCommandUsage());
                        return true;
                    }
                }
            }
        }

        String reason = null;
        if (reasonStartIndex != -1) {
            StringBuilder sb = new StringBuilder();
            for (int i = reasonStartIndex; i < args.length; i++) {
                if (i > reasonStartIndex) sb.append(" ");
                sb.append(args[i]);
            }
            String joined = sb.toString();
            if (joined.startsWith("r:")) {
                reason = joined.substring(2);
            } else if (joined.startsWith("t:")) {
                String templateName = joined.substring(2);
                reason = Main.getInstance().getTemplateManager().getTemplate(templateName);
                if (reason == null) {
                    sender.sendMessage(org.bukkit.ChatColor.RED + "Template '" + templateName + "' not found.");
                    return true;
                }
            }
        } else {
            reason = Main.getInstance().getTemplateManager().getDefaultTemplate();
        }

        // Parse time
        long seconds = manager.parseTime(timeArg);
        if (seconds <= 0) {
            sender.sendMessage(messages.getErrorFormat());
            return true;
        }

        // Start countdown
        if (manager.getTimeLeft() > 0) {
            sender.sendMessage(messages.getAlreadyRunning());
            return true;
        }
        manager.startCountdown(seconds, reason);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            String input = args[0].toLowerCase();

            // Add reload
            if ("reload".startsWith(input) && (sender.hasPermission("stoptimer.stopserver") || sender.isOp())) {
                suggestions.add("reload");
            }

            // Add cancel if countdown running
            if (manager.getTimeLeft() > 0 && "cancel".startsWith(input)) {
                suggestions.add("cancel");
            }

            // Digit suggestions
            if (input.matches("\\d+")) {
                String[] units = {"s", "m", "h"};
                for (String unit : units) {
                    suggestions.add(input + unit);
                }
                return suggestions;
            }

            // Examples
            List<String> examples = Arrays.asList("30s", "1m", "5m", "10m", "1h");
            for (String ex : examples) {
                if (ex.startsWith(input)) suggestions.add(ex);
            }

            // Suggestions for reason/template
            addReasonTemplateSuggestions(input, suggestions);

            return suggestions;
        } else if (args.length == 2) {
            List<String> suggestions = new ArrayList<>();
            String input = args[1].toLowerCase();
            addReasonTemplateSuggestions(input, suggestions);
            return suggestions;
        }
        return new ArrayList<>();
    }

    private void addReasonTemplateSuggestions(String input, List<String> suggestions) {
        if ("r:".startsWith(input)) suggestions.add("r:");
        if (input.startsWith("t:")) {
            String prefix = input.substring(2);
            for (String tmpl : Main.getInstance().getTemplateManager().getTemplateNames()) {
                if (tmpl.toLowerCase().startsWith(prefix)) {
                    suggestions.add("t:" + tmpl);
                }
            }
        } else if ("t:".startsWith(input)) {
            suggestions.add("t:");
        }
    }
}
