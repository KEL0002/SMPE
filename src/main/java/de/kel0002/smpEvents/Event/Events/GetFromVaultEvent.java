package de.kel0002.smpEvents.Event.Events;

import de.kel0002.smpEvents.Commands.Feedback;
import de.kel0002.smpEvents.Event.EventManager;
import de.kel0002.smpEvents.General.TextEditing;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GetFromVaultEvent extends Event{
	private final Material item;

	public GetFromVaultEvent(Material item){
		this.item = item;
	}

	@Override
	public void start_MainGame() {
		super.start_MainGame();
		if (EventManager.get().getCurrentEvent() == null) return;
		for (Player player : joined_players) {
			player.sendMessage(Component.text(
					Feedback.get_msg("event.start_final", "get the item '" + TextEditing.get_clean_item_name(item) + "' from a vault"))
					.append(Feedback.getVoteSkipComponent())
			);
		}
	}

	public Material getItem(){
		return item;
	}
}
