package de.kel0002.smpe;

import de.kel0002.smpe.command.Command;
import de.kel0002.smpe.command.CommandTabCompleter;
import de.kel0002.smpe.events.Event;
import de.kel0002.smpe.events.EventManager;
import de.kel0002.smpe.events.EventRegistrar;
import de.kel0002.smpe.events.types.*;
import de.kel0002.smpe.util.ConfigManager;
import de.kel0002.smpe.util.GeneralUtil;
import de.kel0002.smpe.util.LangManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public final class Main extends JavaPlugin {

    static EventManager eventManager = new EventManager();
    private static Main instance;
    private static LangManager langManager;
    private static ConfigManager configManager;
    static EventRegistrar eventRegistrar = new EventRegistrar();
    static AutoUpdater autoUpdater;
    static int randomStartDenominator;
    static Random random = new Random();


    @Override
    public void onEnable() {

        instance = this;

        langManager = new LangManager(this);
        langManager.load();
        configManager = new ConfigManager(this);
        configManager.load();

        Metrics metrics = new Metrics(this, 25137);

        // Could not find an option to create it on the bstats website, but if i figure it out this should work
        metrics.addCustomChart(new Metrics.MultiLineChart("events_running_multi", () -> {
            Map<String, Integer> valueMap = new HashMap<>();

            valueMap.put("overall", eventManager.all_event_ints().size());
            for (Event event : eventManager.getEvents()) {
                if (event != null) {
                    valueMap.put(event.type(), valueMap.getOrDefault(event.type(), 0) + 1);
                }
            }
            return valueMap;}));

        metrics.addCustomChart(new Metrics.SingleLineChart("events_running", () -> eventManager.all_event_ints().size()));


        this.getCommand("smpe").setExecutor(new Command());
        this.getCommand("smpe").setTabCompleter(new CommandTabCompleter());

        registerEvents();
        randomStartDenominator = configManager.getInt("start_chance_denominator");

        if (!GeneralUtil.isFolia()) {
            getServer().getScheduler().runTaskTimer(this, task -> eventManager.tick_all(), 0L, 1L);
            getServer().getScheduler().runTaskTimer(this, task -> randomStart(), 0L, 1L);
        } else {
            getServer().getGlobalRegionScheduler().runAtFixedRate(this, task -> eventManager.tick_all(), 1L, 1L);
            getServer().getGlobalRegionScheduler().runAtFixedRate(this, task -> randomStart(), 1L, 1L);
        }

        autoUpdater = new AutoUpdater();
        autoUpdater.checkUpdate();
        Bukkit.getPluginManager().registerEvents(autoUpdater, this);
    }



    public static LangManager getLangManager() {return langManager;}
    public static ConfigManager getConfigManager() {return configManager;}
    public static EventManager getEventManager() {return eventManager;}
    public static Main getInstance() {return instance;}
    public static EventRegistrar eventRegistrar() {return eventRegistrar;}

    @Override public void onDisable() {
        eventManager.stopAll();
    }



    public static void registerEvents(){
        // I know that im currently always initializing events with null, null, but this will change in future releases so don't remove

        eventRegistrar.register("advancementEvent",() -> new AdvancementEvent(null, null, null));
        eventRegistrar.register("biomeEvent",() -> new BiomeEvent(null, null, null));
        eventRegistrar.register("effectEvent",() -> new EffectEvent(null, null, null));
        eventRegistrar.register("itemEvent",() -> new ItemEvent(null, null, null));
        eventRegistrar.register("killMobEvent",() -> new KillMobEvent(null, null, null));
        eventRegistrar.register("killPetEvent",() -> new KillPetEvent(null, null));
        eventRegistrar.register("placeBlockAtEvent",() -> new PlaceBlockAtEvent(null, null, null));
        eventRegistrar.register("placeBlockAtInEvent",() -> new PlaceBlockAtInEvent(null, null, null));
        eventRegistrar.register("structureEvent",() -> new StructureEvent(null, null, null));
        eventRegistrar.register("winRaidEvent",() -> new WinRaidEvent(null, null));

    }

    public static void randomStart() {
        if (random.nextInt(0, randomStartDenominator) == 0) {
            if (!configManager.getBoolean("random_start_multiple_events") && !eventManager.all_event_ints().isEmpty()) return;

            if (Bukkit.getOnlinePlayers().size() < configManager.getInt("min_players")) return;

            eventManager.register(eventRegistrar.random());
        }
    }
}
