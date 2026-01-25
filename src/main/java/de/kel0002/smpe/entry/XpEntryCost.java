package de.kel0002.smpe.entry;

import de.kel0002.smpe.Main;
import de.kel0002.smpe.util.CleanText;
import de.kel0002.smpe.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class XpEntryCost extends EntryCost{

    int level;

    public XpEntryCost(Integer level) {
        this.level = randomLevel();
        if (level != null) this.level = level;
    }

    public XpEntryCost() {
        this.level = randomLevel();
    }

    public int randomLevel() {
        double sum = 0;
        int lowest = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {sum += player.getLevel();
            if (lowest == 0 || lowest > player.getLevel()) lowest = player.getLevel();}

        sum += lowest;

        return (int) Math.round((sum/Bukkit.getOnlinePlayers().size() + 1) / (new Random().nextInt(
                Main.getConfigManager().getInt("entrycost.xp.divisor_min"), Main.getConfigManager().getInt("entrycost.xp.divisor_max"))));
    }

    @Override
    public boolean hasEntryCost(Player player) {
        return player.getLevel() >= level;
    }

    @Override
    public void removeEntryCost(Player player) {
        player.setLevel(player.getLevel() - level);
    }

    @Override
    public void giveEntryCost(Player player) {
        player.setLevel(player.getLevel() + level);
    }

    @Override
    public String getFormatted() {
        //return "<sprite:gui:icon/trial_available>ₓ" + GeneralUtil.subscript(String.valueOf(level));
        return "<sprite:items:item/experience_bottle>ₓ" + GeneralUtil.subscript(String.valueOf(level));
    }

    @Override
    public String getRaw() {
        return level + " experience level(s)";
    }

    @Override
    public String name() {return "xp";}

    public static int staticGetWeight() {return Main.getConfigManager().getInt("entrycost." + "xp" + ".weight");}
}
