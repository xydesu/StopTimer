package me.xydesu.stoptimer;

import me.xydesu.stoptimer.Manager.ConfigManager;
import me.xydesu.stoptimer.Manager.Manager;
import me.xydesu.stoptimer.Manager.MessageManager;
import me.xydesu.stoptimer.Manager.PlaceholderManager;
import me.xydesu.stoptimer.Commands.StopServer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Main extends JavaPlugin {
    private static Main instance;
    private Manager manager;
    private MessageManager messageManager;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;

        String language = getConfig().getString("language", "en");
        saveDefaultLangIfAbsent(language);
        FileConfiguration langConfig = loadLangConfig(language);

        messageManager = new MessageManager(langConfig);
        configManager = new ConfigManager(getConfig());
        manager = new Manager(this, messageManager, configManager);

        StopServer stopServerCommand = new StopServer(manager, messageManager, configManager);
        getCommand("stopserver").setExecutor(stopServerCommand);
        getCommand("stopserver").setTabCompleter(stopServerCommand);

        new PlaceholderManager(this, manager, messageManager).register();
    }

    @Override
    public void onDisable() {
        if (manager != null) manager.shutdown();
        instance = null;
    }

    /**
     * Reloads the main config and the active language file, returning the fresh lang FileConfiguration.
     */
    public FileConfiguration reloadLangConfig() {
        reloadConfig();
        String language = getConfig().getString("language", "en");
        saveDefaultLangIfAbsent(language);
        return loadLangConfig(language);
    }

    /**
     * Saves the bundled language file to the plugin data folder if it does not already exist.
     * Falls back to English if the requested language is not bundled.
     */
    private void saveDefaultLangIfAbsent(String language) {
        File langFile = new File(getDataFolder(), "lang/" + language + ".yml");
        if (!langFile.exists()) {
            try {
                saveResource("lang/" + language + ".yml", false);
            } catch (IllegalArgumentException e) {
                getLogger().warning("Language file 'lang/" + language + ".yml' not found in plugin resources. Falling back to 'en'.");
                File enFile = new File(getDataFolder(), "lang/en.yml");
                if (!enFile.exists()) {
                    saveResource("lang/en.yml", false);
                }
            }
        }
    }

    /**
     * Loads and returns the FileConfiguration for the given language.
     * Falls back to English if the language file cannot be found on disk.
     */
    public FileConfiguration loadLangConfig(String language) {
        File langFile = new File(getDataFolder(), "lang/" + language + ".yml");
        if (!langFile.exists()) {
            langFile = new File(getDataFolder(), "lang/en.yml");
        }
        return YamlConfiguration.loadConfiguration(langFile);
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public static Main getInstance() {
        return instance;
    }
}