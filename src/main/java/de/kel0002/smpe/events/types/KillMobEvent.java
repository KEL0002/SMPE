package de.kel0002.smpe.events.types;

import de.kel0002.smpe.entry.EntryCost;
import de.kel0002.smpe.events.Event;
import de.kel0002.smpe.util.CleanText;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.*;

public class KillMobEvent extends Event {
    EntityType mob;

    public KillMobEvent(Integer time, EntryCost entryCost, EntityType mob) {
        super(time, entryCost);
        if (mob == null) this.mob = randomEventMob(); else this.mob = mob;
    }

    @EventHandler
    public void completionChecker(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null || !(event.getEntity().getKiller() instanceof Player player)) return;
        if (!this.getPlayers().contains(player)) return;

        if (event.getEntity().getType().equals(mob)) end(player);
    }
    @Override
    public Map<String, String> getReplacements(){
        Map<String, String> map = new HashMap<>(super.getReplacements());
        map.put("%MOB%", CleanText.clean(mob));
        map.put("%MOB_RAW%", mob.name().toLowerCase());
        map.put("%TR_KEY%", mob.translationKey());

        return map;
    }
    @Override public String type() {return "killMobEvent";}

    public static EntityType randomEventMob() {
        List<EntityType> entities = Arrays.stream(EntityType.values())
                .filter(EntityType::isAlive)
                .toList();

        return entities.get(new Random().nextInt(entities.size()));
    }
}
