package de.kel0002.smpEvents.Commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Feedback {
    final static String prefix = "";

    public static void sendMSG(CommandSender player, String identifier, String parameter){
        player.sendMessage(prefix + get_msg(identifier).replace("{parameter}", parameter));
    }

    public static void sendMSG(CommandSender player, String msg_identifier){
        sendMSG(player, msg_identifier, "");
    }

    public static void broadcastMSG(String identifier, String parameter){
        for (Player player : Bukkit.getOnlinePlayers()){
            sendMSG(player, identifier, parameter);
        }
    }

    public static void broadcastMSG(String identifier){broadcastMSG(identifier, "");}

    public static void sendActionbar(CommandSender player, String identifier, String parameter){
        if (player instanceof Player){
            String rawmsg = prefix + get_msg(identifier).replace("{parameter}", parameter);
            TextComponent component = Component.text(rawmsg);
            player.sendActionBar(component);
        }
    }
    public static void sendActionbar(CommandSender player, String msg_identifier){
        sendActionbar(player, msg_identifier, "");
    }

    public static String get_msg(String identifier, String parameter){
        return get_msg(identifier).replace("{parameter}", parameter);
    }

    public static String get_msg(String identifier){

        return switch (identifier) {

            case "event.in_time_joined" -> "§3The event will start in §e{parameter} seconds§3.";
            case "event.in_time_joined_1" -> "§3The event will start in §e{parameter} second§3.";

            case "event.in_time" -> "§3An event will start in §e{parameter} seconds§3.";
            case "event.in_time_1" -> "§3An event will start in §e{parameter} second§3.";

            case "price.hover" -> "This event requires {parameter} to compete.";

            case "error.no_permission" -> "§4You do not have the required permission to perform this command!";

            case "cmd.usage" -> "§4Incorrect usage: use '/smpe [join | voteskip | leave | start | stop]'.";

            case "cmd.start.already_running" -> "§4Another event is already running. Quit it using §o/smpe stop.";
            case "cmd.stop.no_event" -> "§4There is no event currently running. Start one using §o/smpe start.";

            case "cmd.leave.not_in_event" -> "§4You are currently not in an event.";
            case "cmd.leave.not_now" -> "§4You cannot leave the event now.";
            case "cmd.leave.confirm" -> "§4Leaving the event will not give you your items back! Use '/smpe leave confirm'.";

            case "event.start_final" -> "§bThe event has now started. Your goal is to §e{parameter}§r§b as fast as possible! ";
            case "event.start.not_participating" -> "§7The event has started!";

            case "event.joined" -> "§6{parameter}§b joined the event!";
            case "event.left" -> "§6{parameter}§b left the event!";
            case "join.error.already_joined" -> "§4You have already joined this event.";
            case "join.error.unable" -> "§4You currently can not join the event.";

            case "start.not_enough_players" -> "§bThere were not enough players to start the event.";

            case "event.fireworks_disabled" -> "§4Fireworks are disabled during the current event.";

            case "event.stop" -> "§5The current event was stopped!";
            case "event.won" -> "§6{parameter} §5won the event!";
            case "event.voteskip" -> "§5The current event was voteskipped by the players";
            case "event.player_voteskip" -> "§6{parameter} §7voted for skipping the current event";

            case "event.regiven" -> "§7You were regiven the {parameter} you gave to join the event.";
            case "event.no_voteskip" -> "§4You are unable to voteskip now.";

            case "time.skip" -> "§7The start time for the event has been shortened, as {parameter}% of online players have already joined the event.";

            case "join.error.to_poor" -> "§4You need §o§e{parameter} §4in your Inventory to join the event.";
            default -> "§4§l" + identifier + " (Internal Plugin Error, please create github issue)";
        };
    }

    public static Component getVoteSkipComponent(){
        return Component.text("§7[Click here to VoteSkip]").clickEvent(ClickEvent.runCommand("/smpe voteskip"));
    }
}
