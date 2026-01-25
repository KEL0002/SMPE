package de.kel0002.smpe.entry;

import de.kel0002.smpe.Main;
import de.kel0002.smpe.util.CleanText;
import de.kel0002.smpe.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;
import java.util.stream.Collectors;

public class ItemEntryCost extends EntryCost{
    Material material;
    int amount;

    public ItemEntryCost(Material material, Integer amount) {

        if (material != null) this.material = material; else this.material = randomMaterial();
        if (amount != null) this.amount = amount; else this.amount = getAmount(this.material);
    }

    public ItemEntryCost() {
        this.material = randomMaterial();
        this.amount = getAmount(this.material);
    }

    public Material randomMaterial() {
        List<Map<String, Integer>> mapList = (List<Map<String, Integer>>) Main.getConfigManager().getConfigSection("entrycost.item").getList("material_values");
        List<String> stringList = new ArrayList<>();
        for (Map<String, Integer> map : mapList) {stringList.addAll(map.keySet());}

        //Turn strings into materials
        List<Material> materialList = stringList.stream().map(
                s -> {
                    Material mat = Material.matchMaterial(s.toUpperCase());
                    if (mat == null) Bukkit.getLogger().warning("SMPE config.yml warning: The item '" + s + "' configured in the entrycost section could not be found");
                    return mat;})
                .filter(Objects::nonNull)
                .toList();
        return materialList.get(new Random().nextInt(materialList.size()));
    }


    public int getAmount(Material material) {
        int value = getValue(material);
        int progress = ProgressRater.serverProgress();

        return Math.max(progress/value, 1);
    }

    public int getValue(Material material) {
        // Never change a running system
        Map<String, Integer> materialValues = new HashMap<>();
        for (Map<String, Integer> map : ((List<Map<String, Integer>>) Main.getConfigManager().getConfigSection("entrycost.item").getList("material_values"))){
            materialValues.putAll(map);
        }
        Map<Material, Integer> materialIntegerMap = materialValues.entrySet().stream()
                .map(e -> Map.entry(Objects.requireNonNull(Material.matchMaterial(e.getKey())), e.getValue()))
                .filter(e -> e.getKey() != null)
                .collect(Collectors.toMap(Map.Entry:: getKey, Map.Entry:: getValue));

        return materialIntegerMap.get(material);
    }

    @Override
    public boolean hasEntryCost(Player player) {
        return getMaterialAmount(player, material) >= amount;
    }

    @Override
    public void removeEntryCost(Player player) {
        player.getInventory().removeItemAnySlot(new ItemStack(material, amount));
    }

    @Override
    public void giveEntryCost(Player player) {
        giveItems(player, material, amount);
    }

    @Override
    public String getFormatted() {
        return CleanText.icon(material) + "ₓ" + GeneralUtil.subscript(String.valueOf(amount));
    }

    @Override
    public String getRaw() {
        return amount + " " + CleanText.clean(material);
    }

    @Override
    public String name() {return "item";}

    public static int staticGetWeight() {return Main.getConfigManager().getInt("entrycost." + "item" + ".weight");}






    private void giveItems(Player player, Material material, int amount) {
        ItemStack item = new ItemStack(material, amount);
        PlayerInventory inv = player.getInventory();

        HashMap<Integer, ItemStack> leftover = inv.addItem(item);

        for (ItemStack drop : leftover.values()) {
            player.getWorld().dropItemNaturally(player.getLocation(), drop);
        }
    }

    private static int getMaterialAmount(Player player, Material material) {
        int total = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null) continue;
            if (item.getType() == material) {
                total += item.getAmount();
            }
        }
        return total;
    }

}
