package de.kel0002.smpe.events;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EventManager {
    List<Event> events = new ArrayList<>();

    public boolean is_in_any(Player player) {
        for (Event event : events) {
            if (event == null) continue;
            if (event.getPlayers().contains(player)) return true;
        }
        return false;
    }

    public List<String> all_event_ints(){
        ArrayList<String> all = new ArrayList<>();
        int num = 0;
        for (Event tevent : events) {
            if (tevent != null) {
                all.add(String.valueOf(num));
            }
            num += 1;
        }
        return all;
    }


    public void register(Event event) {
        //ADD EVENT TO THE FIRST AVAILABLE SLOT
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i) == null) {
                events.set(i, event);
                return;
            }
        }
        events.add(event);
    }

    public void unregister(int index) {
        events.set(index, null);
    }

    public void stopAll() {
        for (Event event : events) {
            if (event == null) continue;
            event.end(null,"end.cancel");
        }
    }

    public void tick_all(){
        for (Event event : events) {
            if(event != null) {event.tick();}
        }
    }

    public Integer getInt(Event event) {
        if (!events.contains(event)) return null;

        int num = 0;
        for (Event tevent : events) {
            if (tevent != null && tevent.equals(event)) {
                return num;
            }
            num += 1;
        }
        return null;
    }

    public Event getEvent(int eventInt) {
        if (eventInt >= events.size()) return null;
        return events.get(eventInt);
    }


    public Integer getInt(Player player) {
        Event event = getEvent(player);
        return getInt(event);
    }

    public Event getEvent(Player player) {
        for (Event event : events) {
            if (event == null) continue;
            if (event.getPlayers().contains(player)) return event;
        }
        return null;
    }

    public List<Event> getEvents(){return events;}
}
