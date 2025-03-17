package de.kel0002.smpEvents.Event.Events;

import de.kel0002.smpEvents.Commands.Feedback;
import de.kel0002.smpEvents.General.*;
import de.kel0002.smpEvents.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class Event {
    public Material join_material = null;
    public int join_amount = 0;
    public boolean hasEnded = false;
    public boolean hasPreStarted = false;
    public boolean hasStarted = false;
    public int time_to_start = ConfigManager.start_time() + 1;
    public List<Player> joined_players = new ArrayList<>();
    public List<Player> voteSkippers = new ArrayList<>();
    public int original_playerCount;


    public void start(int price_amount, Material price_material){
        hasPreStarted = true;
        this.join_amount = price_amount;
        this.join_material = price_material;
    }

    public void tick(){
        if (time_to_start <= 0) return;
        time_to_start -= 1;

        if (joined_players.size() >= Math.ceil(Bukkit.getOnlinePlayers().size() * ConfigManager.time_skip_percent() / 100.0) && time_to_start > ConfigManager.skip_time() + 8 && joined_players.size() >= ConfigManager.min_players()){
            time_to_start = ConfigManager.skip_time() + 1;
            Feedback.broadcastMSG("time.skip",  String.valueOf(ConfigManager.time_skip_percent()));
        }
        Set<Integer> send_times = Set.of(15, 10, 5, 4, 3, 2, 1);
        if (send_times.contains(time_to_start) || time_to_start % 30 == 0 && time_to_start != 0){
            for (Player player : Bukkit.getOnlinePlayers()){
                Bukkit.getScheduler().getMainThreadExecutor(Main.getInstance()).execute(() -> player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f));

                if (joined_players.contains(player)){
                    if (time_to_start != 1) Feedback.sendMSG(player, "event.in_time_joined", String.valueOf(time_to_start));
                    else Feedback.sendMSG(player, "event.in_time_joined_1", String.valueOf(time_to_start));
                } else {
                    Component message;
                    if (time_to_start != 1) message = Component.text(Feedback.get_msg("event.in_time", String.valueOf(time_to_start)));
                    else message = Component.text(Feedback.get_msg("event.in_time_1", String.valueOf(time_to_start)));


                    message = message.append(Component.text(" §7[Click to join]").clickEvent(ClickEvent.runCommand("/smpe join")));

                    if (join_amount != 0) message = message.append(Component.text(" §b\uD83D\uDC8E").hoverEvent(
                            HoverEvent.showText(Component.text(Feedback.get_msg("price.hover", join_amount + " " + TextEditing.get_clean_item_name(join_material))))));
                    player.sendMessage(message);
                }
            }
        }
        if (time_to_start == 0) start_MainGame();
    }

    public void start_MainGame(){
        if (joined_players.size() < ConfigManager.min_players()){
            Feedback.broadcastMSG("start.not_enough_players");
            Main.getEventManager().stopEvent();
            return;
        }
        send_to_non_ingame_players(Feedback.get_msg("event.start.not_participating"));
        Bukkit.getScheduler().getMainThreadExecutor(Main.getInstance()).execute(() -> ReallyGeneral.playSound(Sound.BLOCK_NOTE_BLOCK_FLUTE));
        original_playerCount = joined_players.size();
        hasStarted = true;
    }


    public void addPlayer(Player player){
        if (hasStarted) return;
        if (joined_players.contains(player)) {Feedback.sendMSG(player, "join.error.already_joined"); return;}
        boolean removed = InvUtil.remove(player, join_material, join_amount);

        if (!removed){Feedback.sendMSG(player, "join.error.to_poor", join_amount + " " + TextEditing.get_clean_item_name(join_material)); return;}

        joined_players.add(player);
        Feedback.broadcastMSG("event.joined", player.getName());
    }

    public boolean addVoteSkipper(Player player){
        if (!inMainGame()) return false;
        if (!joined_players.contains(player)) return false;
        if (voteSkippers.contains(player)) return false;
        voteSkippers.add(player);
        Feedback.broadcastMSG("event.player_voteskip", player.getName());
        checkVoteskip();
        return true;
    }

    public boolean checkVoteskip(){
        double percent_skipped = (double) voteSkippers.size()/joined_players.size()*100;
        if (percent_skipped >= ConfigManager.skip_percent()){
            Main.getEventManager().stopEvent();
            Feedback.broadcastMSG("event.voteskip");
            return true;
        }
        return false;
    }

    public void removePlayer(Player player){
        if (!joined_players.contains(player)) return;
        joined_players.remove(player);
        voteSkippers.remove(player);
        Feedback.broadcastMSG("event.left", player.getName());
    }

    public void win(Player player){
        InvUtil.give(player, join_material, join_amount*original_playerCount);
        Feedback.broadcastMSG("event.won", player.getName());

        List<Player> losers = joined_players;
        losers.remove(player);

        ReallyGeneral.playSound(losers, Sound.ENTITY_GUARDIAN_DEATH);

        Firework firework = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK_ROCKET);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        FireworkEffect effect = FireworkEffect.builder()
                .with(FireworkEffect.Type.BALL_LARGE)
                .withColor(Color.GREEN, Color.AQUA)
                .withFade(Color.PURPLE)
                .withTrail()
                .withFlicker()
                .build();
        fireworkMeta.addEffect(effect);
        fireworkMeta.setPower(0);
        firework.setFireworkMeta(fireworkMeta);
    }


    public void stop(){}

    public void stop_RegiveItems(){
        for (Player player : joined_players){
            if (join_amount != 0) Feedback.sendMSG(player, "event.regiven", join_amount + " " + TextEditing.get_clean_item_name(join_material));
            InvUtil.give(player, join_material, join_amount);
        }
    }

    public boolean hasEnded(){
        return hasEnded;
    }

    public void send_to_non_ingame_players(String text){
        for (Player player : Bukkit.getOnlinePlayers()){
            if (!joined_players.contains(player)){
                player.sendMessage(text);
            }
        }
    }
    public boolean inMainGame(){
        if (!hasPreStarted) return false;
        if (hasEnded) return false;
        return hasStarted;
    }
    public boolean inEvent(Player player){
        return joined_players.contains(player);
    }
}
