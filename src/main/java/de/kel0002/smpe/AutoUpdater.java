package de.kel0002.smpe;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static de.kel0002.smpe.util.SendUtil.*;

public class AutoUpdater implements Listener {

    String current = Main.getInstance().getPluginMeta().getVersion();
    boolean updateAvailable = false;
    String newest = "";

    public void checkUpdate() {
        Bukkit.getAsyncScheduler().runNow(Main.getInstance(), task -> {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://api.modrinth.com/v2/project/smpe/version"))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JsonArray versions = JsonParser.parseString(response.body()).getAsJsonArray();
                if (versions.isEmpty()) {log("updater.updateCheckFailed"); return;}

                JsonObject latest = versions.get(0).getAsJsonObject();
                newest = latest.get("version_number").getAsString();

                if (newest.equals(current)) return; //Assuming you are not like me and ahead of the latest release

                updateAvailable = true;

                log("updater.console_updateAvailable", Map.of(
                        "%CURRENT%", current,
                        "%NEW%", newest));

            } catch (Exception e) {
                log("updater.updateCheckFailed");
            }
        });
    }


    public boolean updateAvailable() {return updateAvailable;}

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!updateAvailable) return;
        if (event.getPlayer().hasPermission("events.update")) {
            sendMessage("updater.player_updateAvailable", event.getPlayer(), Map.of(
                    "%CURRENT%", current,
                    "%NEW%", newest));
        }
    }
}
