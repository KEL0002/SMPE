package de.kel0002.smpEvents.General;

import de.kel0002.smpEvents.Main;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class ConfigManager {
	public static final FileConfiguration config = Main.getInstance().getConfig();

	public static int start_time(){
		return config.getInt("start_time", 120);
	}
	public static int skip_time(){
		return config.getInt("skip_time", 5);
	}
	public static int skip_percent(){
		int p = config.getInt("voteSkip_percent", 80);
		return Math.min(100, Math.max(p, 0));
	}
	public static int min_players(){
		int p = config.getInt("min_players", 2);
		return Math.max(p, 2);
	}
	public static int time_skip_percent(){
		return Math.min(100, Math.max(config.getInt("time_skip_percent", 100), 0));
	}
	public static boolean no_price(){return config.getBoolean("all_events_free");}

	public static int getProbability(String event){
		return config.getInt("Events." + event + ".probability", 0);
	}
	public static int getSkill(String event){
		return config.getInt("Events." + event + ".skill", 0);
	}

	public static int startChance(){return config.getInt("start_chance", 0);}

	public static HashMap<Material, Integer> price_hashmap() {
		HashMap<Material, Integer> itemValues = new HashMap<>();
		ConfigurationSection itemsSection = config.getConfigurationSection("Items");
		if (itemsSection != null) {
			itemsSection.getValues(false).forEach((key, value) -> {
				try {
					Material material = Material.valueOf(key);
					int price = (Integer) value;
					itemValues.put(material, price);
				} catch (IllegalArgumentException e) {
					Main.logger().warning("Invalid material in config: " + key);
				} catch (ClassCastException e) {
					Main.logger().warning("Invalid price for material " + key + ": " + value);
				}
			});
		}
		return itemValues;
	}
}
