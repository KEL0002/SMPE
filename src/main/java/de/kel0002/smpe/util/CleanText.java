package de.kel0002.smpe.util;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.structure.StructureType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Locale;
import java.util.Map;

public class CleanText {
    public static String capitalizeAfterSpace(String input) {
        char[] chars = input.toCharArray();
        boolean capitalizeNext = true; // capitalize the first character of the string
        for (int i = 0; i < chars.length; i++) {
            if (Character.isWhitespace(chars[i])) {
                capitalizeNext = true;
            } else if (capitalizeNext && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                capitalizeNext = false;
            }
        }
        return new String(chars);
    }

    public static String format(String input) {
        return capitalizeAfterSpace((input.toLowerCase()).replaceAll("_", " "));
    }

    public static String removeMc(String input) {
        return input.startsWith("minecraft:") ? input.substring("minecraft:".length()) : input;
    }


    public static String clean(Material material) {
        return format(material.name());
    }
    public static String clean(Biome biome) {
        return format(biome.getKey().getKey());
    }

    public static String clean(PotionEffectType effect) {
        return format(effect.getKey().getKey());
    }

    public static String clean(EntityType mob) {
        return format(mob.name());
    }

    public static String clean(Vector vector) {
        return (int) vector.getX() + ", " + (int) vector.getY() + ", " + (int) vector.getZ();
    }

    public static String clean(Location location) {
        return (int) location.getX() + ", " + (int) location.getY() + ", " + (int) location.getZ();
    }

    public static String clean(Advancement advancement) {
        return PlainTextComponentSerializer.plainText().serialize(advancement.getDisplay().title());
    }

    public static String clean(World world) {
        switch (world.getEnvironment()) {
            case NORMAL -> {return "the overworld";}
            case NETHER -> {return "the nether";}
            case THE_END -> {return "the end";}
            default -> {return world.getName();}
        }
    }

    public static String clean(StructureType structure) {
        return format(structure.getKey().getKey());
    }





    public static String icon(Material material) {
        if (iconOverwrites().containsKey(material)) {return iconOverwrites().get(material);}

        if (material.isBlock() && material.isSolid() && material.isOccluding()) {return "<sprite:blocks:block/" + material.name().toLowerCase() + ">";}
        if (!material.isBlock()) {return "<sprite:\"minecraft:items\":item/" + material.name().toLowerCase() + ">";}
        //TODO: Add more blocks
        //EIG alles außer stairs, slabs,, etc
        return "?";
    }


    public static String icon (World world) {
        switch (world.getEnvironment()) {
            case NORMAL -> {return "<sprite:blocks:block/grass_block_side>";}
            case NETHER -> {return "<sprite:blocks:block/netherrack>";}
            case THE_END -> {return "<sprite:blocks:block/end_stone>";}
            default -> {return "<sprite:gui:icon/link>";}
        }
    }

    public static String icon(PotionEffectType effect) {
        return "<sprite:gui:mob_effect/" + effect.getKey().getKey() + ">";
    }

    public static Map<Material, String> iconOverwrites() {
        return Map.of(
                Material.GRASS_BLOCK, "<sprite:blocks:block/grass_block_side>"
        );
    }
}
