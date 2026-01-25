package de.kel0002.smpe.events.types;

import de.kel0002.smpe.entry.EntryCost;
import de.kel0002.smpe.events.Event;
import de.kel0002.smpe.util.CleanText;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Bukkit;
import org.bukkit.Registry;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.*;
import java.util.stream.Collectors;

public class BiomeEvent extends Event {
    Biome biome;

    public BiomeEvent(Integer time, EntryCost entryCost, Biome biome) {
        super(time, entryCost);
        if (biome == null) this.biome = randomEventBiome(); else this.biome = biome;
    }

    @EventHandler
    public void completionChecker(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!this.getPlayers().contains(player)) return;

        if (player.getLocation().getWorld().getBiome(player.getLocation()) == biome) {
            end(player);
        }
    }

    @Override
    public Map<String, String> getReplacements(){
        Map<String, String> map = new HashMap<>(super.getReplacements());
        map.put("%BIOME%", CleanText.clean(biome));
        map.put("%BIOME_RAW%", biome.getKey().getKey());
        map.put("%TR_KEY%", biome.translationKey());
        return map;
    }
    @Override public String type() {return "biomeEvent";}


    public static Biome randomEventBiome() {
        List<Biome> playerBiomes = Bukkit.getOnlinePlayers().stream()
                .map(player -> player.getLocation().getBlock().getBiome())
                .distinct().toList();
        List<Biome> biomes = RegistryAccess.registryAccess()
                .getRegistry(RegistryKey.BIOME)
                .stream()
                .filter(biome -> !playerBiomes.contains(biome))
                .toList();

        if (biomes.isEmpty()) return Biome.MUSHROOM_FIELDS;

        return biomes.get(new Random().nextInt(biomes.size()));
    }
}
