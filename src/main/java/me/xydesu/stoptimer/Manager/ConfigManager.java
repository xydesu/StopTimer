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
        return config.getBoolean("BossBar");
    }

    // DefaultTime: time string used when /stopserver is run with no argument
    public String getDefaultTime() {
        return config.getString("DefaultTime", "5m");
    }

}