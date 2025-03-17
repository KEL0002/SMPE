package de.kel0002.smpEvents;

import de.kel0002.smpEvents.Commands.Command;
import de.kel0002.smpEvents.Commands.CommandTabCompleter;
import de.kel0002.smpEvents.Event.EventListeners;
import de.kel0002.smpEvents.Event.EventManager;
import de.kel0002.smpEvents.Event.Events.AdvancementEvent;
import de.kel0002.smpEvents.Event.LeaveListener;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public final class Main extends JavaPlugin {
    private static Main instance;
    private static EventManager eventManager;

    @Override
    public void onEnable() {
        instance = this;
        eventManager = new EventManager();

        saveDefaultConfig();

        this.getCommand("smpe").setExecutor(new Command());
        this.getCommand("smpe").setTabCompleter(new CommandTabCompleter());

        Bukkit.getPluginManager().registerEvents(new EventListeners(), this);
        Bukkit.getPluginManager().registerEvents(new LeaveListener(), this);



        AsyncScheduler scheduler = getServer().getAsyncScheduler();
        scheduler.runAtFixedRate(this, task -> eventManager.tick(), 0, 1, TimeUnit.SECONDS);

        new Metrics(this, 25137);

    }

    public static Main getInstance(){
        return instance;
    }
    public static EventManager getEventManager(){
        return eventManager;
    }

    @Override
    public void onDisable() {
        if (!getEventManager().eventInMainGame()) return;
        if (getEventManager().getCurrentEvent() instanceof AdvancementEvent event) event.regiveAdvancement();
        getEventManager().stopEvent();
    }
    public static Logger logger(){
        return Main.getInstance().getLogger();
    }
}
