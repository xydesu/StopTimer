package me.xydesu.stoptimer.Manager;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigManager {

    private FileConfiguration config;

    public ConfigManager(FileConfiguration config) {
        this.config = config;
    }

    public void reload(FileConfiguration newConfig) {
        this.config = newConfig;
    }

    // NotifyTime.Title.Enable
    public boolean getTitleEnabled() {
        return config.getBoolean("NotifyTime.Title.Enable", true);
    }
    // NotifyTime.Title.FirstRun
    public boolean getTitleFirstRun() {
        return config.getBoolean("NotifyTime.Title.FirstRun");
    }
    // NotifyTime.Title.Seconds
    public List<Integer> getTitleSeconds() {
        return config.getIntegerList("NotifyTime.Title.Seconds");
    }

    // NotifyTime.Message.Enable
    public boolean getMessageEnabled() {
        return config.getBoolean("NotifyTime.Message.Enable", true);
    }
    // NotifyTime.Message.FirstRun
    public boolean getMessageFirstRun() {
        return config.getBoolean("NotifyTime.Message.FirstRun");
    }
    // NotifyTime.Message.Seconds
    public List<Integer> getMessageSeconds() {
        return config.getIntegerList("NotifyTime.Message.Seconds");
    }

    // NotifyTime.Discord.Enable
    public boolean getDiscordEnabled() {
        return config.getBoolean("NotifyTime.Discord.Enable", true);
    }
    // NotifyTime.Discord.FirstRun
    public boolean getDiscordFirstRun() {
        return config.getBoolean("NotifyTime.Discord.FirstRun");
    }
    // NotifyTime.Discord.Seconds
    public List<Integer> getDiscordSeconds() {
        return config.getIntegerList("NotifyTime.Discord.Seconds");
    }

    public boolean getBossbarEnabled() {
        if (config.isBoolean("BossBar")) {
            return config.getBoolean("BossBar");
        }
        return config.getBoolean("BossBar.Enable", true);
    }

    public org.bukkit.boss.BarColor getBossbarColor() {
        if (!config.isConfigurationSection("BossBar")) return org.bukkit.boss.BarColor.RED;
        String colorStr = config.getString("BossBar.Color", "RED");
        try {
            return org.bukkit.boss.BarColor.valueOf(colorStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return org.bukkit.boss.BarColor.RED;
        }
    }

    public org.bukkit.boss.BarStyle getBossbarStyle() {
        if (!config.isConfigurationSection("BossBar")) return org.bukkit.boss.BarStyle.SOLID;
        String styleStr = config.getString("BossBar.Style", "SOLID");
        try {
            return org.bukkit.boss.BarStyle.valueOf(styleStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return org.bukkit.boss.BarStyle.SOLID;
        }
    }

    // DefaultTime: time string used when /stopserver is run with no argument
    public String getDefaultTime() {
        return config.getString("DefaultTime", "5m");
    }

}