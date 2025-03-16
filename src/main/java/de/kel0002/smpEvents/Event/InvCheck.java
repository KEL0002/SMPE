package de.kel0002.smpEvents.Event;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

public class InvCheck {
    public static boolean anyPlayerHasMaterial(Material material){
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (hasMaterialAnywhere(player, material)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasMaterialAnywhere(Player player, Material material) {
        if (hasMaterial(player.getInventory(), material)) {
            return true;
        }

        if (hasMaterial(player.getEnderChest(), material)) {
            return true;
        }

        return false;
    }

    private static boolean hasMaterial(Inventory inventory, Material material) {
        for (ItemStack item : inventory.getContents()) {
            if (item == null) continue;

            if (item.getType() == material) {
                return true;
            }

            if (isShulkerBox(item) && containsInShulkerBox(item, material)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isShulkerBox(ItemStack item) {
        return item.getItemMeta() instanceof BlockStateMeta
                && ((BlockStateMeta) item.getItemMeta()).getBlockState() instanceof ShulkerBox;
    }

    private static boolean containsInShulkerBox(ItemStack shulkerItem, Material material) {
        if (!(shulkerItem.getItemMeta() instanceof BlockStateMeta meta)) return false;

	    if (!(meta.getBlockState() instanceof ShulkerBox shulkerBox)) return false;

	    return hasMaterial(shulkerBox.getInventory(), material);
    }
}
