package de.kel0002.smpEvents.PlayerLevel;

import de.kel0002.smpEvents.General.ConfigManager;
import it.unimi.dsi.fastutil.Pair;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PriceSelector {
	public static Pair<Material, Integer> selectPrice(String eventType){
		int event_skill = ConfigManager.getSkill(eventType);
		HashMap<Material, Integer> materials = ConfigManager.price_hashmap();
		event_skill += random_offset();

		HashMap<Material, Integer> adjusted = adjust(materials, event_skill);

		if (adjusted.isEmpty() || ThreadLocalRandom.current().nextInt(4) == 1) return Pair.of(Material.AIR, 0);

		List<Material> keys = new ArrayList<>(adjusted.keySet());
		Material random = keys.get(ThreadLocalRandom.current().nextInt(keys.size()));

		return Pair.of(random, adjusted.get(random));
	}

	/**
	 * @return Hashmap of Materials with adjusted stack size
	 */
	public static HashMap<Material, Integer> adjust(HashMap<Material, Integer> materials, int event_skill){
		HashMap<Material, Integer> adjusted = new HashMap<>();

		for (Material material : materials.keySet()){
			int material_value = materials.get(material);
			int server_progress = Progress.getServerProgress();

			int amount = (int) ((( (double) 1 / material_value) * 100 * (event_skill + server_progress * 2) / 3)/20);

			amount = stackRound(amount);


			if (amount != 0) adjusted.put(material, amount);

		}

		return adjusted;
	}

	public static int stackRound(int amount){
		if (amount <= 6) {
			return amount;
		} else if (amount <= 32) {
			return Math.round(amount / 4.0f) * 4;
		} else if (amount <= 64) {
			return Math.round(amount / 8.0f) * 8;
		} else if (amount <= 128){
			return (amount / 16) * 16;
		} else {
			return 128;
		}
	}

	public static int random_offset(){
		Random rand = new Random();
		int random = rand.nextInt((70 + 1)) - 35;
		return (int) (0.0008*(random^3));
	}
}
