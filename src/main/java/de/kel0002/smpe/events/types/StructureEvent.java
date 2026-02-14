package de.kel0002.smpe.events.types;

import de.kel0002.smpe.entry.EntryCost;
import de.kel0002.smpe.events.Event;
import de.kel0002.smpe.util.CleanText;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.generator.structure.Structure;
import org.bukkit.generator.structure.StructureType;

import java.util.*;

public class StructureEvent extends Event {
    StructureType structure;

    public StructureEvent(Integer time, EntryCost entryCost, StructureType structure) {
        super(time, entryCost);
        if (structure == null) this.structure = randomEventStructure(); else this.structure = structure;
    }

    @EventHandler
    public void completionChecker(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!this.getPlayers().contains(player)) return;

        if (insideStructure(player.getLocation(), structure)) {
            end(player);
        }
    }

    @Override
    public Map<String, String> getReplacements(){

        NamespacedKey key = structure.getKey();

        Map<String, String> map = new HashMap<>(super.getReplacements());

        map.put("%STRUCTURE%", CleanText.clean(structure));
        map.put("%STRUCTURE_RAW%", structure.getKey().getKey());
        return map;

    }
    @Override public String type() {return "structureEvent";}


    public static StructureType randomEventStructure() {
        List<StructureType> types = new ArrayList<>();

        Location location = PlaceBlockAtInEvent.randomEventLocation();

        for (StructureType type : Registry.STRUCTURE_TYPE.stream().toList()) {
            try {
                location.getWorld().hasStructureAt(location, RegistryAccess.registryAccess().getRegistry(RegistryKey.STRUCTURE).get(type.getKey()));
                types.add(type);
            } catch (Exception ignored) {}
        }

        return types.get(new Random().nextInt(types.size()));
    }

    public static boolean insideStructure(Location location, StructureType structureType) {
        return location.getWorld().hasStructureAt(location, RegistryAccess.registryAccess().getRegistry(RegistryKey.STRUCTURE).get(structureType.getKey()));
    }
}
