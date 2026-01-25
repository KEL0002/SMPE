package de.kel0002.smpe.events.types;

import de.kel0002.smpe.entry.EntryCost;
import de.kel0002.smpe.events.Event;
import de.kel0002.smpe.util.CleanText;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.CreativeCategory;
import static org.bukkit.Material.*;

import java.util.*;

public class ItemEvent extends Event {
    Material material;

    public ItemEvent(Integer time, EntryCost entryCost, Material material) {
        super(time, entryCost);
        if (material == null) this.material = randomEventItem(); else this.material = material;
    }

    @EventHandler
    public void completionChecker(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!this.getPlayers().contains(player)) return;

        if (event.getItem().getItemStack().getType() == material) end(player);

    }
    @Override
    public Map<String, String> getReplacements(){
        Map<String, String> map = new HashMap<>(super.getReplacements());
        map.put("%ITEM%", CleanText.clean(material));
        map.put("%ITEM_RAW%", material.name());
        map.put("%TR_KEY%", material.translationKey());
        if (!CleanText.icon(material).equals("?")) map.put("%ITEM_ICON%", CleanText.icon(material) + " ");
        else map.put("%ITEM_ICON%", "");

        return map;
    }
    @Override public String type() {return "itemEvent";}

    public static Material randomEventItem() {
        List<Material> materials = Arrays.stream(Material.values())
                .filter(Material::isItem)
                .filter(m -> !m.name().endsWith("SPAWN_EGG"))
                .filter(m -> !creativeMaterials().contains(m))
                .toList();

        return materials.get(new Random().nextInt(materials.size()));
    }

    public static List<Material> creativeMaterials(){
        List<Material> materials = new ArrayList<>();
        materials.add(COMMAND_BLOCK);
        materials.add(CHAIN_COMMAND_BLOCK);
        materials.add(REPEATING_COMMAND_BLOCK);
        materials.add(COMMAND_BLOCK_MINECART);
        materials.add(JIGSAW);
        materials.add(STRUCTURE_BLOCK);
        materials.add(STRUCTURE_VOID);
        materials.add(BARRIER);
        materials.add(DEBUG_STICK);
        materials.add(TEST_INSTANCE_BLOCK);
        materials.add(TEST_BLOCK);
        materials.add(LIGHT);
        return materials;
    }
}
