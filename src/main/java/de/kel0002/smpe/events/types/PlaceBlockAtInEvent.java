package de.kel0002.smpe.events.types;

import de.kel0002.smpe.Main;
import de.kel0002.smpe.entry.EntryCost;
import de.kel0002.smpe.events.Event;
import de.kel0002.smpe.util.CleanText;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PlaceBlockAtInEvent extends Event {
    Location location;

    public PlaceBlockAtInEvent(Integer time, EntryCost entryCost, Location location) {
        super(time, entryCost);
        if (location == null) this.location = randomEventLocation(); else this.location = location;
    }

    @EventHandler
    public void completionChecker(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!this.getPlayers().contains(player)) return;

        if (event.getBlock().getLocation().equals(location.toBlockLocation())) {
            end(player);
        }
    }

    @Override
    public Map<String, String> getReplacements(){
        Map<String, String> map = new HashMap<>(super.getReplacements());
        map.put("%LOCATION%", CleanText.clean(location));
        map.put("%WORLD%", CleanText.clean(location.getWorld()));
        map.put("%WORLD_ICON%", CleanText.icon(location.getWorld()));
        return map;
    }
    @Override public String type() {return "placeBlockAtInEvent";}

    public static Location randomEventLocation() {
        double angle = new Random().nextDouble() * Math.PI * 2;
        double distance = new Random().nextInt(0, Main.getConfigManager().getInt("events." + "placeBlockAtInEvent" + ".radius"));

        Vector pos_center = centeredPos();

        int x = (int) ((distance*Math.cos(angle)) + pos_center.getX());
        int z = (int) (distance*Math.sin(angle) + pos_center.getZ());
        int y = new Random().nextInt(0, 255);
        World world = Bukkit.getWorlds().get(new Random().nextInt(Bukkit.getWorlds().size()));

        return new Location(world,x,y,z);
    }

    public static Vector centeredPos() {
        int sum_x = 0;
        int sum_z = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            sum_x += player.getLocation().getBlockX();
            sum_z += player.getLocation().getBlockZ();
        }
        return new Vector(sum_x/Bukkit.getOnlinePlayers().size(), 0, sum_z/Bukkit.getOnlinePlayers().size());
    }

}
