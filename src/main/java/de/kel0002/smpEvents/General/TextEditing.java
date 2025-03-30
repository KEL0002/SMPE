package de.kel0002.smpEvents.General;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.Translatable;
import net.kyori.adventure.translation.TranslationRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class TextEditing {
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

    public static String get_clean_item_name(Material material) {
        return capitalizeAfterSpace(material.name().replace("_", " ").toLowerCase());
    }

    public static String get_clean_advancement_name(Advancement advancement){
        if (advancement.getDisplay() == null) return "Advancement has no Title";
        return PlainTextComponentSerializer.plainText().serialize(advancement.getDisplay().title());
    }
    public static String get_clean_advancement_desc(Advancement advancement){
        return PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(advancement.getDisplay()).description());
    }
    public static String get_clean_entity_name(EntityType entityType){
        return capitalizeAfterSpace(entityType.name().replace("_", " ").toLowerCase());
    }
    public static String get_clean_coordinates(Location location){
        return location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ();
    }
    public static String get_clean_dimensionName(World.Environment environment){
	    return switch (environment) {
		    case NORMAL -> "overworld";
		    case NETHER -> "the nether";
		    case THE_END -> "the end";
		    default -> environment.name().toLowerCase();
	    };
    }
    public static String get_clean_effect_name(PotionEffectType effect){
        return capitalizeAfterSpace(effect.getKey().getKey().replace("_", " "));
    }
    public static String get_clean_biome_name(Biome biome){
        return  capitalizeAfterSpace(biome.toString().replace("_", " ").toLowerCase());

    }
}
