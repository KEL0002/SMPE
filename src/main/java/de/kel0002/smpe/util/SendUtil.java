package de.kel0002.smpe.util;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import de.kel0002.smpe.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendUtil {
    public static LangManager langManager = Main.getLangManager();
    public static ConfigManager configManager = Main.getConfigManager();


    public static void sendMessage(String key, CommandSender player) {sendMessage(key, player, Map.of());}
    public static void sendMessage(String key, CommandSender player, Map<String, String> replacements) {
        player.sendMessage(langManager.get(key, replacements));
    }


    public static void broadcastActionBar(String key, List<Player> players) {broadcastActionBar(key, players, Map.of());}
    public static void broadcastActionBar(String key, List<Player> players, Map<String, String> replacements) {
        for (Player player : players) sendActionBar(key, player, replacements);}

    public static void sendActionBar(String key, Player player) {sendActionBar(key, player, Map.of());}
    public static void sendActionBar(String key, Player player, Map<String, String> replacements) {
        player.sendActionBar(langManager.get(key, replacements));
    }


    public static void broadcastTitle(String key, List<Player> players, Map<String, String> replacements) {
        for (Player player : players){sendTitle(key, player, replacements);}}

    public static void sendTitle(String key, Player player, Map<String, String> replacements) {
        player.showTitle(Title.title(langManager.get(key, replacements), Component.text(""))); //If you want to you can implement subtitles, I am to lazy :D
    }

    public static void broadcastSound(Sound sound) {for (Player player : Bukkit.getOnlinePlayers()) playSound(player, sound);}
    public static void broadcastSound(List<Player> players, Sound sound) { broadcastSound(players, sound, 1, 1);}
    public static void broadcastSound(List<Player> players, Sound sound, float volume, float pitch) {
            for (Player player : players) playSound(player,  sound);}


    public static void playSound(Player player, Sound sound) {
        playSound(player, sound, 1, 1);
    }
    public static void playSound(Player player, Sound sound, float volume, float pitch) {player.playSound(player, sound, volume, pitch);}


    public static void log(String key) {log(key, Map.of());}
    public static void log(String key, Map<String, String> replacements){
        if(configManager.getBoolean("console_logging")) {
            Bukkit.getLogger().info(langManager.getString(key, replacements));
        }
    }
}
