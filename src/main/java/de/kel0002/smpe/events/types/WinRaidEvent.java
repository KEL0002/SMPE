package de.kel0002.smpe.events.types;

import de.kel0002.smpe.entry.EntryCost;
import de.kel0002.smpe.events.Event;
import de.kel0002.smpe.util.CleanText;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.raid.RaidFinishEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class WinRaidEvent extends Event {

    public WinRaidEvent(Integer time, EntryCost entryCost) {
        super(time, entryCost);
    }

    @EventHandler
    public void completionChecker(RaidFinishEvent event) {
        List<Player> winners = new ArrayList<>(event.getWinners());
        winners.retainAll(getPlayers());

        if (winners.isEmpty()) return;
        Player player = winners.get(new Random().nextInt(winners.size()));

        end(player);
    }

    @Override
    public Map<String, String> getReplacements(){
        return new HashMap<>(super.getReplacements());
    }
    @Override public String type() {return "winRaidEvent";}
}
