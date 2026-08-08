package me.xydesu.stoptimer;

import me.xydesu.stoptimer.Manager.ConfigManager;
import me.xydesu.stoptimer.Manager.Manager;
import me.xydesu.stoptimer.Manager.MessageManager;
import me.xydesu.stoptimer.Manager.PlaceholderManager;
import me.xydesu.stoptimer.Manager.TemplateManager;
import me.xydesu.stoptimer.Commands.StopServer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bstats.bukkit.Metrics;

import java.io.File;

public final class Main extends JavaPlugin {
    private static Main instance;
    private Manager manager;
    private MessageManager messageManager;
    private ConfigManager configManager;
    private TemplateManager templateManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;

        String language = getConfig().getString("language", "en");
        saveDefaultLangIfAbsent(language);
        FileConfiguration langConfig = loadLangConfig(language);

        messageManager = new MessageManager(langConfig);
        configManager = new ConfigManager(getConfig());
        templateManager = new TemplateManager(this);
        manager = new Manager(this, messageManager, configManager);

        StopServer stopServerCommand = new StopServer(manager, messageManager, configManager);
        getCommand("stopserver").setExecutor(stopServerCommand);
        getCommand("stopserver").setTabCompleter(stopServerCommand);

        if (org.bukkit.Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderManager(this, manager, messageManager).register();
        }

        // 初始化 bStats 統計
        int pluginId = 33227;
        new Metrics(this, pluginId);
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
        if (templateManager != null) {
            templateManager.reload();
        }
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

    public TemplateManager getTemplateManager() {
        return templateManager;
    }

    public static Main getInstance() {
        return instance;
    }
}