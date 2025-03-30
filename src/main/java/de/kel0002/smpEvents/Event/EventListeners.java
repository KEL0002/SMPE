package de.kel0002.smpEvents.Event;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import de.kel0002.smpEvents.Commands.Feedback;
import de.kel0002.smpEvents.Event.Events.*;
import de.kel0002.smpEvents.Main;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.raid.RaidFinishEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.Random;

public class EventListeners implements Listener {
    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event){
        Event current_event = Main.getEventManager().getCurrentEvent();
        if (!(current_event instanceof ItemEvent itemEvent) || !current_event.inMainGame()) return;
        if (!(event.getEntity() instanceof Player player)) return;
        if (!(event.getItem().getItemStack().getType() == itemEvent.getItem()) || !current_event.inEvent(player)) return;
        Main.getEventManager().stopEvent_Winner(player);
    }


    @EventHandler
    public void onAdvancement(PlayerAdvancementDoneEvent event){
        Event current_event = Main.getEventManager().getCurrentEvent();
        if (!(current_event instanceof AdvancementEvent advancementEvent) || !current_event.inMainGame()) return;
        if (event.getAdvancement().getKey().toString().equals(advancementEvent.getAdvancement().getKey().toString()) && current_event.inEvent(event.getPlayer())) {
            new BukkitRunnable(){
                @Override public void run(){
                    Main.getEventManager().stopEvent_Winner(event.getPlayer());}
            }.runTaskLater(Main.getInstance(), 1);

        }
    }
    @EventHandler
    public void onKill(EntityDeathEvent event){
        Event current_event = Main.getEventManager().getCurrentEvent();
        if (!(current_event instanceof KillMobEvent killMobEvent) || !current_event.inMainGame()) return;
        if (event.getEntity().getKiller() == null || !current_event.inEvent(event.getEntity().getKiller())) return;
        if (event.getEntity().getType() == killMobEvent.getEntityType()){
            Main.getEventManager().stopEvent_Winner(event.getEntity().getKiller());
        }
    }

    @EventHandler
    public void onPet(EntityDeathEvent event){
        Event current_event = Main.getEventManager().getCurrentEvent();
        if (!(current_event instanceof KillPetEvent) || !current_event.inMainGame()) return;
        if (event.getEntity().getKiller() == null || !current_event.inEvent(event.getEntity().getKiller())) return;

        if (!(event.getEntity() instanceof Tameable tameable)) return;
        if (tameable.isTamed() && tameable.getOwner() instanceof Player){
            if (event.getEntity().customName() == null) return;
            Main.getEventManager().stopEvent_Winner(event.getEntity().getKiller());
        }
    }
    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        Event current_event = EventManager.get().getCurrentEvent();
        if (!(current_event instanceof PlaceBlockAtEvent placeBlockAtEvent) || !current_event.inMainGame()) return;
        if(!current_event.inEvent(event.getPlayer())) return;
        if (event.getBlock().getLocation().toVector().equals(placeBlockAtEvent.getLocation().toVector())){
            EventManager.get().stopEvent_Winner(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlace2(BlockPlaceEvent event){
        Event current_event = EventManager.get().getCurrentEvent();
        if (!(current_event instanceof PlaceBlockAtInEvent placeBlockAtInEvent) || !current_event.inMainGame()) return;
        if(!current_event.inEvent(event.getPlayer())) return;
        if (event.getBlock().getLocation().equals(placeBlockAtInEvent.getLocation())){
            EventManager.get().stopEvent_Winner(event.getPlayer());
        }
    }

    @EventHandler
    public void onRaidWin(RaidFinishEvent event){
        if (event.getWinners().isEmpty()) return;
        Player player = event.getWinners().get(new Random().nextInt(event.getWinners().size()));
        Event current_event = EventManager.get().getCurrentEvent();
        if (!(current_event instanceof WinRaidEvent) || !current_event.inMainGame()) return;
        if(!current_event.inEvent(player)) return;
        EventManager.get().stopEvent_Winner(player);
    }

    @EventHandler
    public void onEffect(EntityPotionEffectEvent event){
        if (!(event.getEntity() instanceof Player player)) return;
        if (!(event.getAction() == EntityPotionEffectEvent.Action.ADDED)) return;

        Event current_event = EventManager.get().getCurrentEvent();
        if (!(current_event instanceof EffectEvent effectEvent) || !current_event.inMainGame()) return;
        if(!current_event.inEvent(player)) return;
        if (Objects.requireNonNull(event.getNewEffect()).getType().equals(effectEvent.getEffect())){
            EventManager.get().stopEvent_Winner(player);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Event current_event = EventManager.get().getCurrentEvent();
        if (!(current_event instanceof BiomeEvent biomeEvent) || !current_event.inMainGame()) return;
        if (event.getPlayer().getLocation().getWorld().getBiome(event.getPlayer().getLocation()).equals(biomeEvent.getBiome())){
            EventManager.get().stopEvent_Winner(event.getPlayer());
        }
    }

    @EventHandler
    public void onBoost(PlayerElytraBoostEvent event){
        Event current_event = EventManager.get().getCurrentEvent();
        if (!(current_event instanceof PlaceBlockAtEvent) && !(current_event instanceof PlaceBlockAtInEvent) || !current_event.inMainGame()) return;
        if (current_event.inEvent(event.getPlayer())){
            event.setCancelled(true);
            Feedback.sendMSG(event.getPlayer(), "event.fireworks_disabled");
        }
    }
}
