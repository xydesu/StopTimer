package me.xydesu.stoptimer.Manager;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.stream.Collectors;

import me.xydesu.stoptimer.Utils.TimeUtil;

public class MessageManager {

    private FileConfiguration langConfig;

    public MessageManager(FileConfiguration langConfig) {
        this.langConfig = langConfig;
    }

    public void reload(FileConfiguration newLangConfig) {
        this.langConfig = newLangConfig;
    }

    // Format a duration using the language-configured unit labels (with singular/plural)
    public String formatTime(long seconds) {
        String hourSingular = langConfig.getString("time.hour", "hour");
        String hourPlural = langConfig.getString("time.hours", "hours");
        String minuteSingular = langConfig.getString("time.minute", "minute");
        String minutePlural = langConfig.getString("time.minutes", "minutes");
        String secondSingular = langConfig.getString("time.second", "second");
        String secondPlural = langConfig.getString("time.seconds", "seconds");
        return TimeUtil.formatTime(seconds, hourSingular, hourPlural, minuteSingular, minutePlural, secondSingular, secondPlural);
    }

    public String getReload() {
        return color(langConfig.getString("messages.command.reload", "&aStopTimer reloaded!"));
    }

    // Command messages
    public String getCommandUsage() {
        return color(langConfig.getString("messages.command.usage", "&cUsage: /stopserver <time>"));
    }

    public String getNoPermission() {
        return color(langConfig.getString("messages.command.nopermission", "&cNo permission."));
    }

    public String getAlreadyRunning() {
        return color(langConfig.getString("messages.command.alreadyrunning", "&cA countdown is already running! Use /stopserver cancel first."));
    }

    public String getErrorFormat() {
        return color(langConfig.getString("messages.command.errorformat", "&cInvalid time format."));
    }

    public String getCanceled() {
        return color(langConfig.getString("messages.command.canceled", "&aCountdown cancelled."));
    }

    public String getCancelFail() {
        return color(langConfig.getString("messages.command.cancelfail", "&cNo countdown running."));
    }

    // Notify messages
    public String getTitle(long time, String reason) {
        String t = formatTime(time);
        String msg = langConfig.getString("messages.notify.title", "").replace("%time%", t);
        if (reason != null) msg = msg.replace("%reason%", reason);
        return color(msg);
    }

    public String getSubtitle(long time, String reason) {
        String t = formatTime(time);
        String msg = langConfig.getString("messages.notify.subtitle", "").replace("%time%", t);
        if (reason != null) msg = msg.replace("%reason%", reason);
        return color(msg);
    }

    public String getPlaceholder(long time, String reason) {
        String t = formatTime(time);
        String msg = langConfig.getString("messages.placeholder.message", "").replace("%time%", t);
        if (reason != null) msg = msg.replace("%reason%", reason);
        return color(msg);
    }

    public String getDiscordMessage(long time, String reason) {
        String t = formatTime(time);
        // Discord messages are sent as plain text/Markdown; Minecraft color codes are not applied.
        String msg = langConfig.getString("messages.discord.message", "").replace("%time%", t);
        if (reason != null) msg = msg.replace("%reason%", reason);
        return msg;
    }

    public String getDiscordCancel() {
        // Discord messages are sent as plain text/Markdown; Minecraft color codes are not applied.
        return langConfig.getString("messages.discord.cancel", "");
    }

    public String getBossbarMessage(long time, String reason) {
        String t = formatTime(time);
        String msg = langConfig.getString("messages.bossbar.message", "").replace("%time%", t);
        if (reason != null) msg = msg.replace("%reason%", reason);
        return color(msg);
    }

    public List<String> getMessage(long time, String reason) {
        String t = formatTime(time);
        List<String> lines = langConfig.getStringList("messages.notify.message");
        return lines.stream()
                .map(line -> {
                    String replaced = line.replace("%time%", t);
                    if (reason != null) replaced = replaced.replace("%reason%", reason);
                    return color(replaced);
                })
                .collect(Collectors.toList());
    }

    public List<String> getNotifyCancel() {
        List<String> lines = langConfig.getStringList("messages.notify.cancel");
        return lines.stream()
                .map(this::color)
                .collect(Collectors.toList());
    }

    public String getKickMessage(String reason) {
        String msg = langConfig.getString("messages.notify.kick", "");
        if (reason != null) msg = msg.replace("%reason%", reason);
        return color(msg);
    }

    // Utility
    private String color(String msg) {
        if (msg == null) return "";
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
