package de.kel0002.smpe.events.types;

import de.kel0002.smpe.Main;
import de.kel0002.smpe.entry.EntryCost;
import de.kel0002.smpe.events.Event;
import de.kel0002.smpe.util.CleanText;
import io.papermc.paper.registry.keys.GameRuleKeys;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.GameRules;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.print.Paper;
import java.util.*;

public class AdvancementEvent extends Event {
    Advancement advancement;
    Map<Player, Set<String>> toRegive = new HashMap<>();

    public AdvancementEvent(Integer time, EntryCost entryCost, Advancement advancement) {
        super(time, entryCost);
        if (advancement == null) this.advancement = randomEventAdvancement(); else this.advancement = advancement;
    }

    @Override
    public void manage_start() {
        super.manage_start();

        //SAVE PROGRESS
        for (Player player : super.getPlayers()) {
            toRegive.put(player, new HashSet<>(player.getAdvancementProgress(advancement).getAwardedCriteria()));

            for (String criteria : player.getAdvancementProgress(advancement).getAwardedCriteria()) {
                player.getAdvancementProgress(advancement).revokeCriteria(criteria);
            }
        }
    }

    @Override
    public boolean removePlayer(Player player) {
        boolean success = super.removePlayer(player); // Just realized that it always returns true, but may change in the future
        if (success) restoreProgress(player);
        return success;
    }

    public void restoreProgress(Player player) {
        if (!toRegive.containsKey(player)) return;

        AdvancementProgress progress = player.getAdvancementProgress(advancement);

        Boolean wasEnabled = player.getWorld().getGameRuleValue(GameRules.SHOW_ADVANCEMENT_MESSAGES);
        if (wasEnabled == null) wasEnabled = true; //idk when this would ever happen, but just to be sure
        player.getWorld().setGameRule(GameRules.SHOW_ADVANCEMENT_MESSAGES, false);

        for (String criteria : toRegive.get(player)) {
            if (!progress.getAwardedCriteria().contains(criteria)) {
                progress.awardCriteria(criteria);
            }
        }
        toRegive.remove(player);

        player.getWorld().setGameRule(GameRules.SHOW_ADVANCEMENT_MESSAGES, wasEnabled);
    }

    public void onLeave(PlayerQuitEvent event) {
        if (super.getPlayers().contains(event.getPlayer())) {
            removePlayer(event.getPlayer());
        }
    }

    @Override
    public void end(Player player, String message) {
        super.end(player, message);
        for (Player player1 : new HashSet<>(toRegive.keySet())) {
            if (player1.equals(player)) continue;
            restoreProgress(player1);
        }
    }

    @EventHandler
    public void completionChecker(PlayerAdvancementDoneEvent event) {
        Player player = event.getPlayer();
        if (!this.getPlayers().contains(player)) return;

        if (advancement.getKey().equals(event.getAdvancement().getKey())) {
            end(player);
        }
    }

    @Override
    public Map<String, String> getReplacements(){
        Map<String, String> map = new HashMap<>(super.getReplacements());
        map.put("%ADVANCEMENT%", CleanText.clean(advancement));
        map.put("%ADVANCEMENT_RAW%", advancement.getKey().getKey());
        map.put("%TR_KEY%", Objects.requireNonNull(advancement.getDisplay()).displayName() instanceof Translatable translatable
                        ? translatable.translationKey() : CleanText.clean(advancement));
        map.put("%DESCRIPTION%", PlainTextComponentSerializer.plainText().serialize(advancement.getDisplay().description()));
        // TODO: DESCRIPTION TRANSLATION KEY
        return map;
    }

    @Override public String type() {return "advancementEvent";}


    public static Advancement randomEventAdvancement() {
        List<Advancement> advancements = new ArrayList<>();
        Bukkit.getServer().advancementIterator().forEachRemaining(iadvancement ->{
            if (iadvancement.getDisplay() != null && iadvancement.getCriteria().size() > 1) {
                advancements.add(iadvancement);
            }
        });
        return advancements.get(new Random().nextInt(advancements.size()));
    }
}
