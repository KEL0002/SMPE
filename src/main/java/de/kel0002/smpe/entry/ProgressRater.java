package de.kel0002.smpe.entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import static org.bukkit.Material.*;

import java.util.Arrays;
import java.util.Map;

public class ProgressRater {

    public static int variedServerProgress() {
        int p = serverProgress();
        return (int) Math.round(p*(0.7 + Math.random()* 0.6));
    }


    public static int serverProgress() {
        int sum = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            sum += playerProgress(player);}
        double avg = (double) sum / Bukkit.getOnlinePlayers().size();
        //TODO: Some fancy formular to be biased towards people with less progress
        return (int) Math.round(avg);
    }

    public static int playerProgress(Player player) {
        int progress = 0;

        //   █████╗ ██████╗ ███╗   ███╗ ██████╗ ██████╗
        //  ██╔══██╗██╔══██╗████╗ ████║██╔═══██╗██╔══██╗
        //  ███████║██████╔╝██╔████╔██║██║   ██║██████╔╝
        //  ██╔══██║██╔══██╗██║╚██╔╝██║██║   ██║██╔══██╗
        //  ██║  ██║██║  ██║██║ ╚═╝ ██║╚██████╔╝██║  ██║
        //  ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝     ╚═╝ ╚═════╝ ╚═╝  ╚═╝

        Map<Material, Integer> helmetValues = Map.of(
                NETHERITE_HELMET, 128,
                DIAMOND_HELMET, 100,
                IRON_HELMET, 64,
                GOLDEN_HELMET, 32,
                CHAINMAIL_HELMET, 20,
                COPPER_HELMET, 10
        ); progress += getItemTypeProgress(player, helmetValues);

        Map<Material, Integer> chestplateValues = Map.of(
                NETHERITE_CHESTPLATE, 128,
                DIAMOND_CHESTPLATE, 100,
                IRON_CHESTPLATE, 64,
                GOLDEN_CHESTPLATE, 32,
                CHAINMAIL_CHESTPLATE, 20,
                COPPER_CHESTPLATE, 10
        ); progress += getItemTypeProgress(player, chestplateValues);

        Map<Material, Integer> leggingsValues = Map.of(
                NETHERITE_LEGGINGS, 128,
                DIAMOND_LEGGINGS, 100,
                IRON_LEGGINGS, 64,
                GOLDEN_LEGGINGS, 32,
                CHAINMAIL_LEGGINGS, 20,
                COPPER_LEGGINGS, 10
        ); progress += getItemTypeProgress(player, leggingsValues);

        Map<Material, Integer> bootsValues = Map.of(
                NETHERITE_BOOTS, 128,
                DIAMOND_BOOTS, 100,
                IRON_BOOTS, 64,
                GOLDEN_BOOTS, 32,
                CHAINMAIL_BOOTS, 20,
                COPPER_BOOTS, 10
        ); progress += getItemTypeProgress(player, bootsValues);

        if (playerInvContains(player.getInventory(), ELYTRA)) progress += 100;

        //  ████████╗ ██████╗  ██████╗ ██╗     ███████╗
        //  ╚══██╔══╝██╔═══██╗██╔═══██╗██║     ██╔════╝
        //     ██║   ██║   ██║██║   ██║██║     ███████╗
        //     ██║   ██║   ██║██║   ██║██║     ╚════██║
        //     ██║   ╚██████╔╝╚██████╔╝███████╗███████║
        //     ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝╚══════╝

        Map<Material, Integer> swordValues = Map.of(
                NETHERITE_SWORD, 128,
                DIAMOND_SWORD, 100,
                IRON_SWORD, 64,
                COPPER_SWORD, 32,
                GOLDEN_SWORD, 20,
                STONE_SWORD, 16
        ); progress += getItemTypeProgress(player, swordValues);

        Map<Material, Integer> pickaxeValues = Map.of(
                NETHERITE_PICKAXE, 128,
                DIAMOND_PICKAXE, 100,
                IRON_PICKAXE, 64,
                COPPER_PICKAXE, 32,
                GOLDEN_PICKAXE, 20,
                STONE_PICKAXE, 16
        ); progress += getItemTypeProgress(player, pickaxeValues);

        Map<Material, Integer> axeValues = Map.of(
                NETHERITE_AXE, 128,
                DIAMOND_AXE, 100,
                IRON_AXE, 64,
                COPPER_AXE, 32,
                GOLDEN_AXE, 20,
                STONE_AXE, 16
        ); progress += getItemTypeProgress(player, axeValues);

        Map<Material, Integer> shovelValues = Map.of(
                NETHERITE_SHOVEL, 128,
                DIAMOND_SHOVEL, 100,
                IRON_SHOVEL, 64,
                COPPER_SHOVEL, 32,
                GOLDEN_SHOVEL, 20,
                STONE_SHOVEL, 16
        ); progress += getItemTypeProgress(player, shovelValues);

        if (playerInvContains(player.getInventory(), MACE)) progress += 100;

        return Math.min(progress, 1024);
    }

    public static int getItemTypeProgress(Player player, Map<Material, Integer> values) {
        Inventory inventory = player.getInventory();

        for (Material material : values.keySet()) {
            if (playerInvContains(player.getInventory(), material)) return values.get(material);
        }
        return 0;
    }

    public static boolean playerInvContains(PlayerInventory inv, Material material) {
        return inv.contains(material)
                || Arrays.stream(inv.getArmorContents()).anyMatch(i -> i != null && i.getType().equals(material))
                || inv.getItemInOffHand().getType().equals(material);
    }
}
