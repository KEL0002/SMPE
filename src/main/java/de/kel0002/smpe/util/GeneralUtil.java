package de.kel0002.smpe.util;

public class GeneralUtil {
    public static String format_time(int ticks) {
        int seconds = ticks/20;
        int h = seconds / 3600;
        int min = (seconds % 3600) / 60;
        int s = seconds % 60;

        if (seconds < 60) {
            return String.format("%ds",s);
        } else if (seconds < 3600) {
            return String.format("%dmin:%02ds",min,s);
        } else {
            return String.format("%dh:%02dmin:%02ds",h,min,s);
        }
    }

    public static boolean isInt(String string){
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String subscript(String s) {
        return s.replaceAll("0", "₀")
                .replaceAll("1", "₁")
                .replaceAll("2", "₂")
                .replaceAll("3", "₃")
                .replaceAll("4", "₄")
                .replaceAll("5", "₅")
                .replaceAll("6", "₆")
                .replaceAll("7", "₇")
                .replaceAll("8", "₈")
                .replaceAll("9", "₉");

    }

    public static boolean isFolia() { // Copied from https://docs.papermc.io/paper/dev/folia-support/
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
