package de.kel0002.smpEvents.Event;

import de.kel0002.smpEvents.Event.Events.*;
import de.kel0002.smpEvents.General.ConfigManager;
import de.kel0002.smpEvents.General.ReallyGeneral;
import de.kel0002.smpEvents.Main;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class EventSelector {

    public static Event randomEvent(){
        String type;
        Event event;

        while (true){
            type = get_random_type();
            Main.getEventManager().setEventTypeString(type);
            if (Objects.equals(type, "Item")){
                Material rm = getRandomItem();
                if (!itemValid(rm)) {continue;}
                event = new ItemEvent(rm);
                break;
            }
            if (Objects.equals(type, "Advancement")){
                event = new AdvancementEvent(getRandomAdvancement());
                break;
            }
            if (Objects.equals(type, "Mob")){
                event = new KillMobEvent(getRandomEntity());
                break;
            }
            if (Objects.equals(type, "PlaceBlockAt")){
                event = new PlaceBlockAtEvent(getRandomLocation(5000, ReallyGeneral.getWorld(World.Environment.NORMAL)));
                break;
            }
            if (Objects.equals(type, "PlaceBlockAtIn")){
                event = new PlaceBlockAtInEvent(getRandomLocation(5000, ReallyGeneral.getWorld(getRandomDimension())));
                break;
            }
            if (Objects.equals(type, "WinRaid")){
                event = new WinRaidEvent();
                break;
            }
            if (Objects.equals(type, "Effect")){
                event = new EffectEvent(getRandomEffect());
                break;
            }
            if (Objects.equals(type, "KillPet")){
                event = new KillPetEvent();
                break;
            }
            if (Objects.equals(type, "Biome")){
                Biome rb = getRandomBiome();
                if (!biomeValid(rb)) continue;
                event = new BiomeEvent(rb);
                break;
            }
        }
        return event;
    }

    public static Biome getRandomBiome(){
        List<Biome> biomes = new ArrayList<>(Registry.BIOME.stream().toList());
        return biomes.get(ThreadLocalRandom.current().nextInt(biomes.size()));
    }

    public static PotionEffectType getRandomEffect(){
        List<PotionEffectType> effectTypes = new ArrayList<>(Registry.POTION_EFFECT_TYPE.stream().toList());
        effectTypes.remove(PotionEffectType.LUCK);
        effectTypes.remove(PotionEffectType.UNLUCK);
        effectTypes.remove(PotionEffectType.INSTANT_DAMAGE);
        effectTypes.remove(PotionEffectType.INSTANT_HEALTH);
        return effectTypes.get(ThreadLocalRandom.current().nextInt(effectTypes.size()));
    }

    public static Material getRandomVaultMaterial(){
        List<Material> materials = Arrays.stream(Material.values())
                .filter(Material::isItem)
                .toList();
        return materials.get(ThreadLocalRandom.current().nextInt(materials.size()));
    }

    public static World.Environment getRandomDimension(){
        World.Environment[] dimensions = World.Environment.values();
        return dimensions[ThreadLocalRandom.current().nextInt(dimensions.length)];
    }

    public static EntityType getRandomEntity(){
        List<EntityType> mobs = Arrays.stream(EntityType.values())
                .filter(EntityType::isAlive)
                .toList();

        return mobs.get(ThreadLocalRandom.current().nextInt(mobs.size()));
    }

    public static Advancement getRandomAdvancement(){
        List<Advancement> advancements = new ArrayList<>();
        Bukkit.getServer().advancementIterator().forEachRemaining(advancement -> {
            if (advancement.getDisplay() != null && advancement.getCriteria().size() == 1) {
                advancements.add(advancement);
            }
        });
        return advancements.get(ThreadLocalRandom.current().nextInt(advancements.size()));
    }


    public static boolean itemValid(Material material){
        return !InvCheck.anyPlayerHasMaterial(material);
    }

    public static boolean biomeValid(Biome biome){
        boolean valid = true;
        for (Player player : Bukkit.getOnlinePlayers()){
            if (player.getLocation().getWorld().getBiome(player.getLocation()).equals(biome)){
                valid = false;
            }
        }
        return valid;
    }

    public static Material getRandomItem(){
        List<Material> materials = Arrays.stream(Material.values())
                .filter(Material::isItem)
                .filter(m -> !m.name().endsWith("SPAWN_EGG"))
                .toList();
        return materials.get(ThreadLocalRandom.current().nextInt(materials.size()));
    }

    public static Location getRandomLocation(int radius, World world) {
        Random random = ThreadLocalRandom.current();

        double angle = random.nextDouble() * Math.PI * 2;
        double distance = random.nextDouble() * radius;
        double x = distance * Math.cos(angle);
        double z = distance * Math.sin(angle);

        x = Math.round(x);
        z = Math.round(z);

        int y = random.nextInt(world.getMaxHeight() - world.getMinHeight());
        y = (y + world.getMinHeight());

        return new Location(world, x, y, z);
    }


    public static ArrayList<String> get_event_types(){
        ArrayList<String> eventTypes = new ArrayList<>();
        eventTypes.add("Advancement");
        eventTypes.add("Effect");
        eventTypes.add("Item");
        eventTypes.add("Mob");
        eventTypes.add("PlaceBlockAt");
        eventTypes.add("PlaceBlockAtIn");
        eventTypes.add("WinRaid");
        eventTypes.add("KillPet");
        eventTypes.add("Biome");
        return eventTypes;
    }


    public static List<String> get_probability_list(){
        ArrayList<String> probability_list = new ArrayList<>();
        for (String string : get_event_types()){
            for (int i = 0; i < ConfigManager.getProbability(string); i++){
                probability_list.add(string);
            }
        }

        return probability_list;

    }
    public static String get_random_type(){
        List<String> list = get_probability_list();
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }
}
