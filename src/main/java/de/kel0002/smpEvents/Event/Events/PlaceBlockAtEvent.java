package de.kel0002.smpEvents.Event.Events;

import de.kel0002.smpEvents.Commands.Feedback;
import de.kel0002.smpEvents.Event.EventManager;
import de.kel0002.smpEvents.General.TextEditing;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlaceBlockAtEvent extends Event{
	private final Location location;

	public PlaceBlockAtEvent(Location location){
		this.location = location;
	}

	@Override
	public void start_MainGame() {
		super.start_MainGame();
		if (EventManager.get().getCurrentEvent() == null) return;
		for (Player player : joined_players) {
			player.sendMessage(Component.text(
							Feedback.get_msg("event.start_final", " place a block at the coordinates " + TextEditing.get_clean_coordinates(location)))
					.append(Feedback.getVoteSkipComponent())
			);
		}
	}

	public Location getLocation(){
		return location;
	}
}
