package de.kel0002.smpEvents.Event.Events;

import de.kel0002.smpEvents.Commands.Feedback;
import de.kel0002.smpEvents.Event.EventManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.entity.Player;

public class KillPetEvent extends Event{
	@Override
	public void start_MainGame() {
		super.start_MainGame();
		if (EventManager.get().getCurrentEvent() == null) return;
		for (Player player : joined_players) {
			player.sendMessage(Component.text("§bThe event has now started. Your goal is to §ekill any ")
					.append(Component.text("§e§o" + "pet").hoverEvent(HoverEvent.showText(Component.text("A mob that is tamed by a player and was given a name with a name tag"))))
					.append(Component.text("§r§b as fast as possible! "))
					.append(Feedback.getVoteSkipComponent()));
		}
	}
}
