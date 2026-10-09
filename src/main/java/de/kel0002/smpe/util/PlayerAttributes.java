package de.kel0002.smpe.util;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerAttributes {
    HashMap<UUID, HashMap<String, Object>> playerMap = new HashMap<>();

    public void set(Player player, String attribute, Object value){
        if (!playerMap.containsKey(player.getUniqueId())) playerMap.put(player.getUniqueId(), new HashMap<>());
        playerMap.get(player.getUniqueId()).put(attribute, value);
    }

    public Object get(Player player, String attribute) {
        if (!playerMap.containsKey(player.getUniqueId())) playerMap.put(player.getUniqueId(), new HashMap<>());
        return playerMap.get(player.getUniqueId()).get(attribute);
    }

    public Map<UUID, Object> getPlayerValueMap(String attribute) {
        Map<UUID, Object> result = new HashMap<>();
        for (Map.Entry<UUID, HashMap<String, Object>> entry : playerMap.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null) continue;
            if (entry.getValue().containsKey(attribute)) {
                result.put(entry.getKey(), entry.getValue().get(attribute));
            }
        }
        return result;
    }

    public void save(File file) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(playerMap);
        } catch (Exception e) {}
    }
    public void load(File file) {
        if (!file.exists()) return;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            playerMap = (HashMap<UUID, HashMap<String, Object>>) in.readObject();
        } catch (Exception e){}
    }
}
