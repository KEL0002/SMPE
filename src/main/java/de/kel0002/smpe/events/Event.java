package de.kel0002.smpe.events;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import de.kel0002.smpe.Main;
import de.kel0002.smpe.entry.EntryCost;
import de.kel0002.smpe.entry.RandomEntryCostGenerator;
import de.kel0002.smpe.util.ConfigManager;
import de.kel0002.smpe.util.GeneralUtil;
import de.kel0002.smpe.util.LangManager;
import static de.kel0002.smpe.util.SendUtil.*;

import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

public class Event implements Listener {

    ConfigManager configManager = Main.getConfigManager();
    LangManager langManager = Main.getLangManager();
    EventManager eventManager = Main.getEventManager();
    boolean logging = configManager.getBoolean("console_logging");

    int time;
    List<Player> players = new ArrayList<>();
    List<Player> voteSkippers = new ArrayList<>();
    EntryCost entryCost;
    int original_playerCount;
    int voteskippers_required = 0;


    public Event (Integer time, EntryCost entryCost) {
        if (time == null) this.time = -Math.abs(configManager.getInt("start_time")*20); else this.time = time;
        if (entryCost == null) this.entryCost = RandomEntryCostGenerator.random(); else this.entryCost = entryCost;
    }


    //  ███████╗██╗   ██╗███████╗███╗   ██╗████████╗    ████████╗██╗ ██████╗██╗  ██╗██╗███╗   ██╗ ██████╗     ███████╗████████╗ █████╗ ██████╗ ████████╗
    //  ██╔════╝██║   ██║██╔════╝████╗  ██║╚══██╔══╝    ╚══██╔══╝██║██╔════╝██║ ██╔╝██║████╗  ██║██╔════╝     ██╔════╝╚══██╔══╝██╔══██╗██╔══██╗╚══██╔══╝
    //  █████╗  ██║   ██║█████╗  ██╔██╗ ██║   ██║          ██║   ██║██║     █████╔╝ ██║██╔██╗ ██║██║  ███╗    ███████╗   ██║   ███████║██████╔╝   ██║
    //  ██╔══╝  ╚██╗ ██╔╝██╔══╝  ██║╚██╗██║   ██║          ██║   ██║██║     ██╔═██╗ ██║██║╚██╗██║██║   ██║    ╚════██║   ██║   ██╔══██║██╔══██╗   ██║
    //  ███████╗ ╚████╔╝ ███████╗██║ ╚████║   ██║          ██║   ██║╚██████╗██║  ██╗██║██║ ╚████║╚██████╔╝    ███████║   ██║   ██║  ██║██║  ██║   ██║
    //  ╚══════╝  ╚═══╝  ╚══════╝╚═╝  ╚═══╝   ╚═╝          ╚═╝   ╚═╝ ╚═════╝╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝ ╚═════╝     ╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝

    public void tick() {
        time += 1;
        if (time < 0) {manage_pregame();
        } else if (time == 0) {manage_start();
        } else {manage_main();}
    }

    public void manage_pregame() {
        int time_to_start = -time;

        //SEND NOTIFICATION
        if (configManager.getIntList("notify_times").stream().anyMatch(i -> i == time_to_start/20.0)
                || ((time_to_start/20.0) % configManager.getInt("multiples_of") == 0 && configManager.getInt("multiples_of") != 0)) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!players.contains(player)) {
                    //SEND MESSAGE TO ALL PLAYERS NOT IN EVENT
                    sendMessage(player, "pregame.not_joined");
                } else {
                    //SEND MESSAGE TO ALL PLAYERS IN EVENT
                    sendMessage(player, "pregame.joined");
                }
            }
        }
        broadcastActionBar("pregame.actionbar", players, getReplacements());

        // Check timeskip
        if ((double) Bukkit.getOnlinePlayers().size()*((double) configManager.getInt("timeskip_percent")/100) <= players.size()
        && time_to_start > 200) {
            time = -101;
        }
    }

    public void manage_start() {
        //Check if anybody has left before the event started (onLeave isnt registered before the start)
        for (Player player : players) {
            if (!player.isOnline()) {removePlayer(player);}
        }

        //End if not enough Players
        original_playerCount = players.size();
        if (original_playerCount < configManager.getInt("min_players") || original_playerCount == 1 || original_playerCount == 0) {
            end(null, "start.not_enough_players");
            return;
        }

        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!players.contains(player)) {
                //SEND MESSAGE TO ALL PLAYERS NOT IN EVENT
                sendMessage(player, "start.started_not_participating");
            } else {
                //SEND MESSAGE TO ALL PLAYERS IN EVENT
                sendMessage(player, "start.started_participating");
            }
        }

        broadcastSound(players, Sound.BLOCK_BEACON_ACTIVATE);

        checkVoteskip(); // Just to update the amount of players required to voteskip
        log("start");
    }

    public void manage_main() {
        broadcastActionBar("main.actionbar", players, getReplacements());
    }

    //  ███████╗██╗   ██╗███████╗███╗   ██╗████████╗    ████████╗██╗ ██████╗██╗  ██╗██╗███╗   ██╗ ██████╗     ███████╗███╗   ██╗██████╗
    //  ██╔════╝██║   ██║██╔════╝████╗  ██║╚══██╔══╝    ╚══██╔══╝██║██╔════╝██║ ██╔╝██║████╗  ██║██╔════╝     ██╔════╝████╗  ██║██╔══██╗
    //  █████╗  ██║   ██║█████╗  ██╔██╗ ██║   ██║          ██║   ██║██║     █████╔╝ ██║██╔██╗ ██║██║  ███╗    █████╗  ██╔██╗ ██║██║  ██║
    //  ██╔══╝  ╚██╗ ██╔╝██╔══╝  ██║╚██╗██║   ██║          ██║   ██║██║     ██╔═██╗ ██║██║╚██╗██║██║   ██║    ██╔══╝  ██║╚██╗██║██║  ██║
    //  ███████╗ ╚████╔╝ ███████╗██║ ╚████║   ██║          ██║   ██║╚██████╗██║  ██╗██║██║ ╚████║╚██████╔╝    ███████╗██║ ╚████║██████╔╝
    //  ╚══════╝  ╚═══╝  ╚══════╝╚═╝  ╚═══╝   ╚═╝          ╚═╝   ╚═╝ ╚═════╝╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝ ╚═════╝     ╚══════╝╚═╝  ╚═══╝╚═════╝


    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        if (players.contains(event.getPlayer())) {
            removePlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void onFirework(PlayerElytraBoostEvent event) {
        if (players.contains(event.getPlayer()) && configManager.getBoolean("events." + type() + ".fireworks_disabled")) {
            event.setCancelled(true);
            sendMessage(event.getPlayer(), "error.fireworks_disabled");
        }
    }


    public boolean addPlayer(Player player) {

        //CHECK IF PLAYER HAS ALREADY JOINED
        if (players.contains(player)) {
            sendMessage(player, "pregame.join_already_joined");
            playSound(player, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO);
            return false;
        }

        //CHECK IF EVENT IS RUNNING
        if (time >= 0) {
            playSound(player, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO);
            sendMessage(player, "error.event_already_running"); return false;}

        //CHECK IF PLAYER IS IN OTHER EVENT
        if (eventManager.is_in_any(player)) {
            sendMessage(player, "pregame.join_already_joined_other",
                Map.of("%OTHER_EVENT_NUMBER%", String.valueOf(eventManager.getInt(player))));
            playSound(player, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO);
            return false;
        }

        //CHECK IF PLAYER HAS ENTRY COST
        if (!entryCost.hasEntryCost(player)) {
            playSound(player, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO);
            sendMessage(player, "pregame.join_no_entry_cost");
            return false;
        }

        entryCost.removeEntryCost(player);
        players.add(player);
        broadcast("pregame.join", Map.of("%PLAYER%", player.getName()));
        broadcastSound(players, Sound.BLOCK_NOTE_BLOCK_PLING);
        log("join", Map.of("%PLAYER%", player.getName()));
        return true;
    }

    public boolean removePlayer(Player player) {

        broadcast("main.leave", players, Map.of("%PLAYER%", player.getName()));
        broadcastSound(players, Sound.BLOCK_CHAIN_BREAK);
        log("leave", Map.of("%PLAYER%", player.getName()));
        players.remove(player);
        voteSkippers.remove(player);

        if (time < 0) {
            entryCost.giveEntryCost(player);
        }

        checkVoteskip();
        return true;
    }

    public boolean addVoteSkipper(Player player) {
        if (voteSkippers.contains(player)) {
            sendMessage(player, "main.voteskip_already_done");
            return false;
        }

        if (time <= 0) {
            sendMessage(player, "main.voteskip_before_start");
            return false;
        }

        voteSkippers.add(player);
        broadcast("main.voteskip", Map.of("%PLAYER%", player.getName()));

        checkVoteskip();
        return true;
    }

    public void checkVoteskip() {
        if (time < 0) return;

        // Percent rounded up with a maximum of PLAYERS-1 and a minimum of 1
        voteskippers_required = Math.max(
                Math.min(
                        (int) Math.ceil((double) configManager.getInt("voteskip_percent") / 100 * players.size()), players.size()
                                - 1),
                                    1);

        if (voteSkippers.size() >= voteskippers_required) {
            end(null, "end.voteskipped");
        }

        // Also check for event end because of low players
        if (players.isEmpty()) { // I believe impossible, but just to check
            end(null);
        } else if (players.size() == 1) {
            end(players.getFirst());
        }
    }

    public void end(Player player, String message) {
            if (player != null) {
                entryCost.giveWin(player, original_playerCount);
                broadcast("end.winner", players, Map.of("%PLAYER%", player.getName()));
                playWinEffect(player);
                log("end", Map.of("%MESSAGE%", langManager.getString("end.winner", Map.of("%PLAYER%", player.getName()))));

            } else {
                for (Player player1 : players) {
                    entryCost.giveEntryCost(player1);
                }
                broadcast(message, players);
                log("end", Map.of("%MESSAGE%", langManager.getString(message)));
            }

            broadcastActionBar("end.actionbar", players, getReplacements());
            broadcastSound(players, Sound.BLOCK_BEACON_DEACTIVATE);

            HandlerList.unregisterAll(this); //Unregister the listener
            eventManager.unregister(eventManager.getInt(this));
    }
    public void end(Player player) {end(player, "end.cancel");}

    public void playWinEffect(Player winner) {
        broadcastTitle("end.title_winner", players, new HashMap<>(getReplacements()) {{ put("%PLAYER%", winner.getName());}});

        Firework firework = (Firework) winner.getWorld().spawnEntity(winner.getLocation(), EntityType.FIREWORK_ROCKET);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        FireworkEffect effect = FireworkEffect.builder()
                .with(FireworkEffect.Type.BALL_LARGE)
                .withColor(Color.GREEN, Color.fromRGB(78, 36, 8))
                .withColor(Color.ORANGE)
                .withTrail()
                .withFlicker()
                .build();

        fireworkMeta.addEffect(effect);
        fireworkMeta.setPower(0);
        firework.setFireworkMeta(fireworkMeta);
    }


    public Map<String, String> getReplacements() {
        Map<String, String> map = new HashMap<>();
        map.put("%EVENT_NUMBER%", String.valueOf(eventManager.getInt(this) != null ? eventManager.getInt(this) : "UNKNOWN EVENT NUMBER"));
        map.put("%TIME%", GeneralUtil.format_time(Math.abs(time)));
        map.put("%EVENT_TYPE%", type());
        map.put("%EVENT_GOAL%", getGoal());
        map.put("%PLAYERCOUNT%", String.valueOf(players.size()));
        map.put("%PLAYERCOUNT_LOWER%", GeneralUtil.subscript(String.valueOf(players.size())));
        map.put("%VOTESKIPPER_AMOUNT%", String.valueOf(voteSkippers.size()));
        map.put("%VOTESKIPPERS_REQUIRED%", String.valueOf(voteskippers_required));
        map.put("%ENTRYCOST_FORMATTED%", entryCost.getFormatted());
        map.put("%ENTRYCOST_RAW%", entryCost.getRaw());
        map.put("%NOTICE%", getNotice());
        map.put("%ACTIONBAR_GOAL%", getActionBarGoal());

        return map;
    }

    public String getGoal() {return langManager.getString("start.goals." + type());}
    public String getActionBarGoal (){return langManager.getString("main.actionbar_goal." + type());}

    public int getTime() {return time;}
    public List<Player> getPlayers() {return players;}
    public String type() {return "undefinedEvent";}
    public String getNotice() {return langManager.getString("start.notice." + type());}



    //MESSAGE SENDING UTIL (Same as SendUtil.java but always includes default replacements)

    public void broadcast(String key) {for (Player player : Bukkit.getOnlinePlayers()) {sendMessage(player, key);}}
    public void broadcast(String key, Map<String, String> replacements) {for (Player player : Bukkit.getOnlinePlayers()) {sendMessage(player, key, replacements);}}
    public void broadcast(String key, List<Player> playerList) {for (Player player : playerList) {sendMessage(player, key);}}
    public void broadcast(String key, List<Player> playerList, Map<String, String> replacements) {
        for (Player player : playerList) {
            sendMessage(player, key, replacements);
        }
    }

    public void sendMessage(CommandSender sender, String key) {sender.sendMessage(langManager.get(key, getReplacements()));}
    public void sendMessage(CommandSender sender, String key, Map<String, String> replacements) {
        Map<String, String> merged = new HashMap<>(replacements);
        merged.putAll(getReplacements());
        sender.sendMessage(langManager.get(key, merged));
    }

    public void log(String key, Map<String, String> replacements) {
        if(logging) {
            Map<String, String> merged = new HashMap<>(replacements);
            merged.putAll(getReplacements());
            Bukkit.getLogger().info(langManager.getString("console." + key, merged));
        }
    }
    public void log(String key) {log(key, Map.of());}
}
