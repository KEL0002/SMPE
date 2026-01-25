package de.kel0002.smpe.entry;

import de.kel0002.smpe.Main;
import de.kel0002.smpe.util.CleanText;
import de.kel0002.smpe.util.GeneralUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;

public abstract class EntryCost {

    public EntryCost() {}

    public boolean hasEntryCost(Player player) {
        return true;
    }

    public void removeEntryCost(Player player) {}

    public void giveEntryCost(Player player) {}

    public void giveWin(Player player, int players) {
        for (int i = 0; i < players; i++) {
            giveEntryCost(player);
        }
    }

    public String getFormatted() {return "";}
    public String getRaw() {return "";}
    public String name() {return "";}
    public int getWeight() {return Main.getConfigManager().getInt("entrycost." + name() + ".weight");}
}
