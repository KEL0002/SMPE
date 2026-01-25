package de.kel0002.smpe.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class LangManager {
    JavaPlugin plugin;
    FileConfiguration lang;
    FileConfiguration defaults;

    public LangManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        plugin.saveResource("lang.yml", false);
        File file = new File(plugin.getDataFolder(), "lang.yml");
        lang = YamlConfiguration.loadConfiguration(file);

        defaults = YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource("lang.yml")));

        //Add missing defaults
        boolean changed = false;
        for (String key : defaults.getKeys(true)) {
            if (!lang.contains(key)) {
                lang.set(key, defaults.get(key));
                changed = true;
            }
        }
        if (changed) try {lang.save(file);} catch (Exception ignored) {}
    }

    public Component get(String key) {
        return MiniMessage.miniMessage().deserialize(getString(key));
    }

    public Component get(String key, Map<String, String> replacements) {
        return MiniMessage.miniMessage().deserialize(getString(key, replacements));
    }

    public String getString(String key, Map<String, String> replacements) {
        String text = lang.getString(key, "INTERNAL PLUGIN ERROR");

        Map<String, String> merged = new HashMap<>(replacements);

        ConfigurationSection customSection = lang.getConfigurationSection("custom");
        Map<String, String> customMap = customSection.getKeys(false).stream()
                .collect(Collectors.toMap(
                        key1 -> key1,
                        key1 -> customSection.getString(key1, "ERROR: THERE IS SOMETHING WRONG WITH THE 'CUSTOM' SECTION FO THE PLUGINS YML FILE")
                ));

        merged.putAll(customMap);


        //Remove "'" from advancements and items tend to break stuff
        for (String entry : merged.keySet()) {
            if ("%ADVANCEMENT%".equals(entry)) {
                merged.put(entry, merged.get(entry).replaceAll("'", "")); // Advancements sometimes contain a "'", which will break the minimessage format, so banning them is the only real option

            }
        }

        Map<String, String> underscored = new HashMap<>();

        for (Map.Entry<String, String> entry : merged.entrySet()) {
            String rawKey = entry.getKey();
            String value = entry.getValue();

            if (!rawKey.startsWith("%") || !rawKey.endsWith("%") || value == null) continue;

            String inner = rawKey.substring(1, rawKey.length() - 1);

            if (!value.isEmpty()) {
                underscored.put("%_" + inner + "%", " " + value);
                underscored.put("%" + inner + "_%", value + " ");
                underscored.put("%_" + inner + "_%", " " + value + " ");
            } else {
                underscored.put("%_" + inner + "%", "");
                underscored.put("%" + inner + "_%", "");
                underscored.put("%_" + inner + "_%", "");
            }

        }

        merged.putAll(underscored);

        while (merged.keySet().stream().anyMatch(text::contains)) {
            for (Map.Entry<String, String> entry : merged.entrySet()) {
                text = text.replace(entry.getKey(), entry.getValue());
            }
        }
        return text;
    }
    public String getString(String key) {return getString(key, Map.of());}

}
