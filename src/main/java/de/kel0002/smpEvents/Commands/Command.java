package de.kel0002.smpEvents.Commands;

import de.kel0002.smpEvents.Event.EventManager;
import de.kel0002.smpEvents.Event.LeaveListener;
import de.kel0002.smpEvents.General.ConfigManager;
import de.kel0002.smpEvents.Main;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Command implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        EventManager eventManager = Main.getEventManager();
        if (args.length >= 1){
            switch (args[0]){
                case "start":
                    if (!sender.hasPermission("events.manage")){ Feedback.sendMSG(sender, "error.no_permission"); return false;}
                    boolean success_start = eventManager.startEvent();
                    if (success_start){return true;}
                    else {Feedback.sendMSG(sender, "cmd.start.already_running"); return false;}

                case "stop":
                    if (!sender.hasPermission("events.manage")){ Feedback.sendMSG(sender, "error.no_permission"); return false;}
                    boolean success_stop = eventManager.stopEvent();
                    if (success_stop){ Feedback.broadcastMSG("event.stop");
                        return true;}
                    else {Feedback.sendMSG(sender, "cmd.stop.no_event"); return false;}

                case "join":
                    if (!sender.hasPermission("events.join")){ Feedback.sendMSG(sender, "error.no_permission"); return false;}
                    if (!(sender instanceof Player player)){ Feedback.sendMSG(sender, "error.not_a_player"); return false;}
                    if (eventManager.getCurrentEvent() == null) { Feedback.sendMSG(sender,"join.error.unable"); return false;}
                    if (eventManager.getCurrentEvent().hasStarted) { Feedback.sendMSG(sender,"join.error.unable"); return false;}
                    eventManager.getCurrentEvent().addPlayer(player);
                    return true;

                case "voteskip":
                    if (!(sender instanceof Player player)) return false;
                    if (!sender.hasPermission("events.voteskip")){ Feedback.sendMSG(sender, "error.no_permission"); return false;}
                    if (!eventManager.eventInMainGame()){
                        Feedback.sendMSG(player, "event.no_voteskip");
                        return false;
                    }
                    boolean voteskip_success = eventManager.getCurrentEvent().addVoteSkipper(player);
                    if (!voteskip_success) Feedback.sendMSG(player, "event.no_voteskip");
                    return true;

                case "configcheck":
                    if (!sender.hasPermission("events.manage")){ Feedback.sendMSG(sender, "error.no_permission"); return false;}
                    sendConfigCheck(sender);
                    return true;

                case "leave":
                    if (!(sender instanceof Player player)) return false;

                    if (!eventManager.eventInMainGame()) {Feedback.sendMSG(sender,"cmd.leave.not_now"); return false;}
                    if (!eventManager.getCurrentEvent().inEvent(player)){Feedback.sendMSG(player, "cmd.leave.not_in_event"); return false;}

                    if ((args.length < 2) || !(args[1].equals("confirm"))) {Feedback.sendMSG(player,"cmd.leave.confirm"); return false;}
                    eventManager.getCurrentEvent().removePlayer(player);
                    LeaveListener.postLeaveCheck();
                    return true;

                default:
                    Feedback.sendMSG(sender, "cmd.usage");
                    return false;
            }
        } else {
            Feedback.sendMSG(sender, "cmd.usage");
        }
        return false;
    }
    public void sendConfigCheck(CommandSender player){
        player.sendMessage("------------------");
        player.sendMessage("Min players: " + ConfigManager.min_players());
        player.sendMessage("Time skip %: " + ConfigManager.time_skip_percent());
        player.sendMessage("VoteSkip %: " + ConfigManager.skip_percent());
        player.sendMessage("Skip Time: " + ConfigManager.skip_time());
        player.sendMessage("Start Time: " + ConfigManager.start_time());
        player.sendMessage("Start Chance: " + ConfigManager.startChance());
        player.sendMessage("------------------");
    }
}
