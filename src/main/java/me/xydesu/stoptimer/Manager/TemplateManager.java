package me.xydesu.stoptimer.Manager;

import me.xydesu.stoptimer.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class TemplateManager {

    private final Main plugin;
    private FileConfiguration templateConfig;
    private File templateFile;

    public TemplateManager(Main plugin) {
        this.plugin = plugin;
        setup();
    }

    public void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }
        templateFile = new File(plugin.getDataFolder(), "templates.yml");
        if (!templateFile.exists()) {
            try {
                templateFile.createNewFile();
                templateConfig = YamlConfiguration.loadConfiguration(templateFile);
                // Add some default templates
                templateConfig.set("templates.default", "Server stopping");
                templateConfig.set("templates.update", "Server updating");
                templateConfig.set("templates.maintenance", "Server maintenance");
                templateConfig.save(templateFile);
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create templates.yml!");
            }
        }
        templateConfig = YamlConfiguration.loadConfiguration(templateFile);
    }

    public void reload() {
        if (templateFile == null) {
            templateFile = new File(plugin.getDataFolder(), "templates.yml");
        }
        templateConfig = YamlConfiguration.loadConfiguration(templateFile);
    }

    public String getTemplate(String name) {
        if (templateConfig.contains("templates." + name)) {
            return templateConfig.getString("templates." + name);
        }
        return null; // Return null if template doesn't exist
    }

    public String getDefaultTemplate() {
        return templateConfig.getString("templates.default", "");
    }

    public java.util.Set<String> getTemplateNames() {
        if (templateConfig.isConfigurationSection("templates")) {
            return templateConfig.getConfigurationSection("templates").getKeys(false);
        }
        return java.util.Collections.emptySet();
    }
}
