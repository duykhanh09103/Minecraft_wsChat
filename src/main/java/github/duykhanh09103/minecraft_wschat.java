package github.duykhanh09103;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public final class minecraft_wschat extends JavaPlugin {
    public static wsClient client;
    FileConfiguration config = getConfig();
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        this.getCommand("reconnect").setExecutor(new wsClientReconnect());
        config.addDefault("Uri", "ws://localhost:8080");
        config.addDefault("enable",true);
        config.addDefault("ListenOnPlayerChat", true);
        config.addDefault("ListenOnPlayerJoin", true);
        config.addDefault("ListenOnPlayerQuit", true);
        config.options().copyDefaults(true);
        saveConfig();
        if (config.getBoolean("enable")) {
            try {
                client = new wsClient(new URI(Objects.requireNonNull(config.getString("Uri"))));
                client.setConnectionLostTimeout(30);
                client.connectBlocking();

            } catch (URISyntaxException | InterruptedException e) {
                e.printStackTrace();
            }
            //delay send ws by sending 5s after server run
            Bukkit.getScheduler().runTaskLater(this, () -> {
                if (client != null && client.isOpen()) {
                    client.send("Server is on!");
                }
            }, 100L);
        }
        if(!config.getBoolean("enable")){
            getLogger().info("config is set to not enable! Shutting down plugin...");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        if (client != null && client.isOpen()) {
            client.send("Server stopped!");
            client.close();
        }
    }
}
