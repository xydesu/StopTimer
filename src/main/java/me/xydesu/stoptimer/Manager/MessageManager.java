package me.xydesu.stoptimer.Manager;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

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
    public String getTitle() {
        return color(langConfig.getString("messages.notify.title", ""));
    }

    public String getSubtitle(long time) {
        String t = formatTime(time);
        return color(langConfig.getString("messages.notify.subtitle", "").replace("%time%", t));
    }

    public String getPlaceholder(long time) {
        String t = formatTime(time);
        return color(langConfig.getString("messages.placeholder.message", "").replace("%time%", t));
    }

    public String getDiscordMessage(long time) {
        String t = formatTime(time);
        // Discord messages are sent as plain text/Markdown; Minecraft color codes are not applied.
        return langConfig.getString("messages.discord.message", "").replace("%time%", t);
    }

    public String getDiscordCancel() {
        // Discord messages are sent as plain text/Markdown; Minecraft color codes are not applied.
        return langConfig.getString("messages.discord.cancel", "");
    }

    public String getBossbarMessage(long time) {
        String t = formatTime(time);
        return color(langConfig.getString("messages.bossbar.message", "").replace("%time%", t));
    }

    public List<String> getMessage(long time) {
        String t = formatTime(time);
        List<String> lines = langConfig.getStringList("messages.notify.message");
        return lines.stream()
                .map(line -> color(line.replace("%time%", t)))
                .toList();
    }

    public List<String> getNotifyCancel() {
        List<String> lines = langConfig.getStringList("messages.notify.cancel");
        return lines.stream()
                .map(this::color)
                .toList();
    }

    public String getKickMessage() {
        return color(langConfig.getString("messages.notify.kick", ""));
    }

    // Utility
    private String color(String msg) {
        if (msg == null) return "";
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
