package de.kel0002.smpEvents.Event.Events;

import de.kel0002.smpEvents.Commands.Feedback;
import de.kel0002.smpEvents.Event.EventManager;
import de.kel0002.smpEvents.General.TextEditing;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class KillMobEvent extends Event{
	private final EntityType entityType;

	public KillMobEvent(EntityType entity_type){
		this.entityType = entity_type;
	}

	@Override
	public void start_MainGame() {
		super.start_MainGame();
		if (EventManager.get().getCurrentEvent() == null) return;
		for (Player player : joined_players) {
			player.sendMessage(Component.text(
					Feedback.get_msg("event.start_final", "kill the mob: '" + TextEditing.get_clean_entity_name(entityType) + "'"))
					.append(Feedback.getVoteSkipComponent())
			);
		}
	}

	public EntityType getEntityType(){
		return entityType;
	}
}
