package de.kel0002.smpEvents.Event.Events;

import de.kel0002.smpEvents.Commands.Feedback;
import de.kel0002.smpEvents.Event.EventManager;
import de.kel0002.smpEvents.General.TextEditing;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class BiomeEvent extends Event{
	private final Biome biome;

	public BiomeEvent(Biome biome){
		this.biome = biome;
	}

	@Override
	public void start_MainGame() {
		super.start_MainGame();
		if (EventManager.get().getCurrentEvent() == null) return;
		for (Player player : joined_players) {
			player.sendMessage(Component.text(
							Feedback.get_msg("event.start_final", "enter the biome '" + TextEditing.get_clean_biome_name(biome) + "'"))
					.append(Feedback.getVoteSkipComponent())
			);
		}
		send_to_non_ingame_players(Feedback.get_msg("event.start.not_participating"));
	}

	public Biome getBiome(){
		return biome;
	}
}