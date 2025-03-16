package de.kel0002.smpEvents.Event.Events;

import de.kel0002.smpEvents.Commands.Feedback;
import de.kel0002.smpEvents.Event.EventManager;
import de.kel0002.smpEvents.General.TextEditing;
import de.kel0002.smpEvents.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AdvancementEvent extends Event {
	private final Advancement advancement;
	public List<Player> had_advancement = new ArrayList<>();

	public AdvancementEvent(Advancement advancement){
		this.advancement = advancement;
	}

	@Override
	public void start_MainGame() {
		super.start_MainGame();
		if (EventManager.get().getCurrentEvent() == null) return;
		for (Player player : joined_players) {
			player.sendMessage(Component.text("§bThe event has now started. Your goal is to §eget the advancement '")
					.append(Component.text("§e§o" + TextEditing.get_clean_advancement_name(advancement)).hoverEvent(HoverEvent.showText(Component.text(TextEditing.get_clean_advancement_desc(advancement)))))
					.append(Component.text("§e'§r§b as fast as possible! "))
					.append(Feedback.getVoteSkipComponent())
					.append(Component.text(" §7ⓘ").hoverEvent(HoverEvent.showText(Component.text("The advancement was temporarily removed so that you can get it again.")))));
		}
		revokeAdvancement();
	}

	@Override
	public void stop(){
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), this::regiveAdvancement, 1L);
	}

	@Override
	public void removePlayer(Player player){
		super.removePlayer(player);
		regiveAdvancementOnly(player);
		had_advancement.remove(player);
	}

	@Override
	public boolean checkVoteskip(){
		boolean shouldskip = super.checkVoteskip();
		if (shouldskip){
			regiveAdvancement();
		}

		return shouldskip;
	}

	public Advancement getAdvancement(){
		return advancement;
	}

	public void revokeAdvancement(){
		for (Player player : joined_players){
			if (player.getAdvancementProgress(advancement).isDone()){
				had_advancement.add(player);
				for (String criteria : player.getAdvancementProgress(advancement).getAwardedCriteria()){
					player.getAdvancementProgress(advancement).revokeCriteria(criteria);
				}
			}
		}
	}

	public void regiveAdvancement(){
		for (Player player : had_advancement){
			addAdvancementSilent(player, advancement);
		}
	}
	public void regiveAdvancementOnly(Player player){
		if (!had_advancement.contains(player)) return;
		addAdvancementSilent(player, advancement);
	}

	public void addAdvancementSilent(Player player, Advancement advancement){
		boolean announce = Boolean.TRUE.equals(player.getWorld().getGameRuleValue(GameRule.ANNOUNCE_ADVANCEMENTS));
		if (announce) player.getWorld().setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
		AdvancementProgress progress = player.getAdvancementProgress(advancement);
		for (String criteria : progress.getRemainingCriteria()){
			progress.awardCriteria(criteria);
		}
		if (announce) player.getWorld().setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, true);
	}
}
