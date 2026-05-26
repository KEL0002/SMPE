package de.kel0002.smpe.util;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

public class ConfigManager {
    JavaPlugin plugin;
    FileConfiguration config;
    FileConfiguration defaults;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        if (!new File(plugin.getDataFolder(), "config.yml").exists()) {
            plugin.saveResource("config.yml", false);}
        File file = new File(plugin.getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(file);

        defaults = YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource("config.yml")));

        //Add missing defaults
        boolean changed = false;
        for (String key : defaults.getKeys(true)) {
            if (!config.contains(key)) {
                config.set(key, defaults.get(key));
                changed = true;
            }
        }
        if (changed) try {config.save(file);}   catch (Exception e) {e.printStackTrace();}
    }

    public Object get(String key) {
        return config.get(key);
    }
    public boolean getBoolean(String key) {return config.getBoolean(key);}
    public Integer getInt(String key) {return config.getInt(key);}
    public double getDouble(String key) {return config.getDouble(key);}
    public String getString(String key) {return config.getString(key);}
    public List<Integer> getIntList(String key) {return config.getIntegerList(key);}
    public ConfigurationSection getConfigSection (String key){return config.getConfigurationSection(key);}
}
