package de.kel0002.smpEvents.Event;

import de.kel0002.smpEvents.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener {
	@EventHandler
	public void onLeave(PlayerQuitEvent event){
		if (!Main.getEventManager().eventInMainGame()) return;
		Main.getEventManager().getCurrentEvent().removePlayer(event.getPlayer());
		postLeaveCheck();
	}
	public static void postLeaveCheck(){
		if (!Main.getEventManager().eventInMainGame()) return;
		if (Main.getEventManager().getCurrentEvent().joined_players.size() == 1){
			Main.getEventManager().stopEvent_Winner(Main.getEventManager().getCurrentEvent().joined_players.getFirst());
		}
		if (Main.getEventManager().eventInMainGame()){
			Main.getEventManager().getCurrentEvent().checkVoteskip();
		}
	}
}
