package de.kel0002.smpe.command;

import de.kel0002.smpe.Main;
import de.kel0002.smpe.entry.ProgressRater;
import de.kel0002.smpe.events.Event;
import de.kel0002.smpe.events.EventManager;
import de.kel0002.smpe.util.GeneralUtil;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Sound;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import static de.kel0002.smpe.util.SendUtil.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Command implements CommandExecutor {

    EventManager eventManager = Main.getEventManager();


    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 0) { return handleWrongUsage(sender); }
        switch (args[0].toLowerCase()) {
            case "list" -> {
                List<Event> events = Main.getEventManager().getEvents();
                events.removeIf(Objects::isNull);
                sendMessage("info.list", sender,
                                        Map.of("%AMOUNT%", String.valueOf(events.size()),
                                                "%LIST%", events.stream()
                                                        .map(event -> eventManager.getInt(event) + ": " +
                                                                (event.getTime() < 0 ? "???Event" : event.type())
                                                                + " (" + GeneralUtil.format_time(event.getTime()) + ", "
                                                        + "<head:b>ₓ" + GeneralUtil.subscript(String.valueOf(event.getPlayers().size())) + ")"
                                                        )
                                                        .collect(Collectors.joining(", \n"))));
                return true;
            }

            case "start" -> {return handleStart(sender);}

            case "stop" -> {return handleStop(sender, args.length > 1 ? args[1] : null);}

            case "join" -> {return handleJoin(sender, args.length > 1 ? args[1] : null);}

            case "leave" -> {return handleLeave(sender);}

            case "voteskip" -> {return handleVoteskip(sender);}

            // Hidden utility commands
            case "echo" -> sender.sendMessage(MiniMessage.miniMessage().deserialize(String.join(" ", Arrays.stream(args).toList())));
            case "sprogress" ->{ if (sender.hasPermission("events.manage")) sender.sendMessage("Server progress: " + ProgressRater.serverProgress());}

            default -> handleWrongUsage(sender);
        }
        return false;
    }

    public boolean handleWrongUsage(CommandSender sender) {
        if (sender instanceof Player p)playSound(p, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO);
        sendMessage("error.usage", sender);
        return false;
    }

    public boolean handleStart(CommandSender sender) {
        if (doesNotHavePermission(sender,"events.manage")) {
            if (sender instanceof Player p) playSound(p, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO);
            sendMessage("error.no_permission", sender);return false;}

        Main.getEventManager().register(Main.eventRegistrar().random());
        return true;
    }

    public boolean handleStop(CommandSender sender, String eventIntStr){
        if (doesNotHavePermission(sender,"events.manage")) {
            if (sender instanceof Player p)playSound(p, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO);
            sendMessage( "error.no_permission", sender);return false;}

        if ("all".equalsIgnoreCase(eventIntStr)) {
            eventManager.stopAll();
            sendMessage("command_feedback.stop_all", sender);
            return true;
        }

        if (!GeneralUtil.isInt(eventIntStr)){
            sendMessage("error.usage_stop", sender);
            if (sender instanceof Player p)playSound(p, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO);
            return false;
        }
        int eventInt = Integer.parseInt(eventIntStr);
        Event event = eventManager.getEvent(eventInt);

        if (event != null) {
            event.end(null,"end.cancel");
            sendMessage("command_feedback.stop_single", sender, Map.of("%EVENT_NUMBER%", eventIntStr));
            return true;
        } else {
            sendMessage("error.event_out_of_range", sender);
            if (sender instanceof Player p)playSound(p, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO);
            return false;
        }
    }

    public boolean handleJoin(CommandSender sender, String eventIntStr) {
        if (isNotPlayer(sender)) return false;

        if (doesNotHavePermission(sender,"events.join")) {
            if (sender instanceof Player p)playSound(p, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO);
            sendMessage("error.no_permission", sender);return false;}

        if (!GeneralUtil.isInt(eventIntStr)) {
            sendMessage("error.usage_join", sender);
            if (sender instanceof Player p)playSound(p, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO);
            return false;
        }

        int eventInt = Integer.parseInt(eventIntStr);
        Event event = eventManager.getEvent(eventInt);

        if (event != null) {
            // Checks if this is valid done inside Event itself
            return event.addPlayer((Player) sender);
        } else {
            sendMessage("error.event_out_of_range", sender);
            if (sender instanceof Player p)playSound(p, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO);
            return false;
        }
    }

    public boolean handleLeave(CommandSender sender) {
        if (isNotPlayer(sender)) return false;

        Event event = eventManager.getEvent((Player) sender);

        if (event != null) {
            return event.removePlayer((Player) sender);
        } else {
            sendMessage("error.not_in_event", sender);
            if (sender instanceof Player p)playSound(p, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO);
            return false;
        }
    }

    public boolean handleVoteskip(CommandSender sender) {
        if (isNotPlayer(sender)) return false;

        if (doesNotHavePermission(sender,"events.voteskip")) {
            if (sender instanceof Player p)playSound(p, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO);
            sendMessage("error.no_permission", sender);return false;}

        Event event = eventManager.getEvent((Player) sender);

        if (event != null) {
            return event.addVoteSkipper((Player) sender);
        } else {
            Player p = (Player) sender;
            playSound(p, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO);
            sendMessage( "error.not_in_event", sender);
            return false;
        }

    }


    public boolean isNotPlayer(CommandSender sender) {
        if (sender instanceof Player) {
            return false;
        } else {
            sendMessage("error.not_a_player", sender);
            return true;
        }
    }

    public boolean doesNotHavePermission(CommandSender sender, String permission) {
        return !sender.hasPermission(permission);
    }
}
