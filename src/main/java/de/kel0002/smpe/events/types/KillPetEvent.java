package de.kel0002.smpe.events.types;

import de.kel0002.smpe.entry.EntryCost;
import de.kel0002.smpe.events.Event;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.raid.RaidFinishEvent;

import java.util.*;

public class KillPetEvent extends Event {

    public KillPetEvent(Integer time, EntryCost entryCost) {
        super(time, entryCost);
    }

    @EventHandler
    public void completionChecker(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null || !(event.getEntity().getKiller() instanceof Player player)) return;
        if (!this.getPlayers().contains(player)) return;

        if (!(event.getEntity() instanceof Tameable tameable)) return;

        if (tameable.isTamed() && tameable.getOwner() instanceof Player) end(player);
    }

    @Override
    public Map<String, String> getReplacements(){return new HashMap<>(super.getReplacements());}
    @Override public String type() {return "killPetEvent";}
}
