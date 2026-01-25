package de.kel0002.smpe.events.types;

import de.kel0002.smpe.entry.EntryCost;
import de.kel0002.smpe.events.Event;
import de.kel0002.smpe.util.CleanText;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Bukkit;
import org.bukkit.Registry;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class EffectEvent extends Event {
    PotionEffectType effect;

    public EffectEvent(Integer time, EntryCost entryCost, PotionEffectType effect) {
        super(time, entryCost);
        if (effect == null) this.effect = randomEventEffect(); else this.effect = effect;
    }

    @EventHandler
    public void completionChecker(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!this.getPlayers().contains(player)) return;

        if (!event.getAction().equals(EntityPotionEffectEvent.Action.ADDED)) return;

        if (effect.equals(event.getNewEffect() != null ? event.getNewEffect().getType() : null)) end(player);


    }

    @Override
    public Map<String, String> getReplacements(){
        Map<String, String> map = new HashMap<>(super.getReplacements());
        map.put("%EFFECT%", CleanText.clean(effect));
        map.put("%EFFECT_RAW%", effect.getKey().getKey());
        map.put("%TR_KEY%", effect.translationKey());
        map.put("%EFFECT_ICON%", CleanText.icon(effect));
        return map;
    }
    @Override public String type() {return "effectEvent";}


    public static PotionEffectType randomEventEffect() {
        List<PotionEffectType> effects = new ArrayList<>(Registry.POTION_EFFECT_TYPE.stream().toList());

        effects.remove(PotionEffectType.LUCK);
        effects.remove(PotionEffectType.UNLUCK);
        effects.remove(PotionEffectType.INSTANT_DAMAGE);
        effects.remove(PotionEffectType.INSTANT_HEALTH);

        return effects.get(new Random().nextInt(effects.size()));
    }
}
