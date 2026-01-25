package de.kel0002.smpe.entry;

import de.kel0002.smpe.Main;

public class NoneEntryCost extends EntryCost{
    public NoneEntryCost () {}

    @Override
    public String name() {return "none";}

    public static int staticGetWeight() {return Main.getConfigManager().getInt("entrycost." + "none" + ".weight");}
}
