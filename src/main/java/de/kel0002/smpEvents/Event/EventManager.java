package de.kel0002.smpEvents.Event;

import de.kel0002.smpEvents.Event.Events.Event;
import de.kel0002.smpEvents.General.ConfigManager;
import de.kel0002.smpEvents.Main;
import de.kel0002.smpEvents.PlayerLevel.PriceSelector;
import it.unimi.dsi.fastutil.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.concurrent.ThreadLocalRandom;


public class EventManager {

    public String eventTypeString = "";

    public static EventManager get(){
        return Main.getEventManager();
    }

    private Event current_event = null;

    public void tick(){
        if (current_event != null) current_event.tick();

        if (ThreadLocalRandom.current().nextInt(ConfigManager.startChance() + 1) == 1 && Bukkit.getOnlinePlayers().size() >= ConfigManager.min_players()) startEvent();

    }

    public boolean startEvent (){
        if (eventRunning()) return false;
        current_event = EventSelector.randomEvent();

        Pair<Material, Integer> price = PriceSelector.selectPrice(eventTypeString);

        current_event.start(price.second(), price.first());
        return true;
    }

    public boolean justStopEvent(boolean regiveItems){
        if (!eventRunning()) return false;

        if (!regiveItems){current_event.stop();}
        else {current_event.stop_RegiveItems();}
        current_event = null;
        return true;
    }

    public boolean stopEvent(){
        return justStopEvent(true);
    }

    public void stopEvent_Winner(Player player){
        current_event.win(player);
        justStopEvent(false);

    }

    public Event getCurrentEvent(){
        return current_event;
    }

    public boolean eventRunning(){
        if (current_event == null) return false;
        return !current_event.hasEnded();
    }
    public boolean eventInMainGame(){
        if (current_event == null) return false;
        return current_event.inMainGame();
    }

    public void setEventTypeString(String string){
        eventTypeString = string;
    }

}
