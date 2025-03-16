package de.kel0002.smpEvents.Event.Events;

import de.kel0002.smpEvents.Commands.Feedback;
import de.kel0002.smpEvents.Event.EventManager;
import de.kel0002.smpEvents.General.TextEditing;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class EffectEvent extends Event{
	private final PotionEffectType effect;

	public EffectEvent(PotionEffectType effect){
		this.effect = effect;
	}

	@Override
	public void start_MainGame() {
		super.start_MainGame();
		if (EventManager.get().getCurrentEvent() == null) return;
		for (Player player : joined_players) {
			player.sendMessage(Component.text(
					Feedback.get_msg("event.start_final", "get the effect '" + TextEditing.get_clean_effect_name(effect) + "'"))
					.append(Feedback.getVoteSkipComponent())
			);
		}
		send_to_non_ingame_players(Feedback.get_msg("event.start.not_participating"));
	}

	public PotionEffectType getEffect(){
		return effect;
	}
}
