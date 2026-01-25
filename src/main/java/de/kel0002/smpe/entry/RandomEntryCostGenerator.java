package de.kel0002.smpe.entry;

import de.kel0002.smpe.Main;
import de.kel0002.smpe.util.ConfigManager;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomEntryCostGenerator {
    static ConfigManager configManager = Main.getConfigManager();

    public static EntryCost random() {
        int noneW = NoneEntryCost.staticGetWeight();
        int itemW = ItemEntryCost.staticGetWeight();
        int xpW = XpEntryCost.staticGetWeight();

        int total = noneW + itemW + xpW;
        int r = new Random().nextInt(total);

        if (r < noneW) return new NoneEntryCost();
        r -= noneW;
        if (r < itemW) return new ItemEntryCost(null, null);
        return new XpEntryCost(null);
    }
}
