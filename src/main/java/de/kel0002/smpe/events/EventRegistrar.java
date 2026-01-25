package de.kel0002.smpe.events;

import de.kel0002.smpe.Main;
import org.bukkit.Bukkit;

import java.util.*;
import java.util.function.Supplier;

public class EventRegistrar {

    Map<String, Supplier<? extends Event>> events = new HashMap<>();

    public void register(String type, Supplier<? extends Event> event) {
        events.put(type, event);
    }

    public Event random() {
        if (events.isEmpty()) return null;
        List<Supplier<? extends Event>> eventsWeighted = new ArrayList<>();
        for (String type : events.keySet()) {
            int weight = Main.getConfigManager().getInt("events." + type + ".weight");
            for (int i = 0; i< weight; i++) {
                eventsWeighted.add(events.get(type));
            }
        }
        return eventsWeighted.get(new Random().nextInt(eventsWeighted.size())).get();
    }
}
