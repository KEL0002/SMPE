package de.kel0002.smpEvents.Event.Events;

import de.kel0002.smpEvents.Commands.Feedback;
import de.kel0002.smpEvents.Event.EventManager;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class WinRaidEvent extends Event{

	public WinRaidEvent(){}

	@Override
	public void start_MainGame() {
		super.start_MainGame();
		if (EventManager.get().getCurrentEvent() == null) return;
		for (Player player : joined_players) {
			player.sendMessage(Component.text(
							Feedback.get_msg("event.start_final", "win a raid"))
					.append(Feedback.getVoteSkipComponent())
			);
		}
	}

}
