package github.duykhanh09103;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public final class minecraft_wschat extends JavaPlugin {
    public static wsClient client;
    FileConfiguration config = getConfig();
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        this.getCommand("reconnect").setExecutor(new wsClientReconnect());
        config.addDefault("Uri", "ws://localhost:8080");
        config.addDefault("statusInTab",true);
        config.addDefault("listen.enable",false);
        config.addDefault("listen.onPlayerChat", true);
        config.addDefault("listen.onPlayerJoin", true);
        config.addDefault("listen.onPlayerQuit", true);
        config.addDefault("listen.onAdvancementDone",true);
        config.addDefault("listen.onPlayerDeath",true);
        config.options().copyDefaults(true);
        saveConfig();
        if (config.getBoolean("listen.enable")) {
            try {
                client = new wsClient(new URI(Objects.requireNonNull(config.getString("Uri"))));
                client.setConnectionLostTimeout(5);
                client.connectBlocking();

            } catch (URISyntaxException | InterruptedException e) {
                e.printStackTrace();
            }
            Bukkit.getScheduler().runTaskLater(this, () -> {
                if (client != null && client.isOpen()) {
                    client.send("Server is on!");
                }
            }, 20L);
        }
        if(!config.getBoolean("listen.enable")){
            getLogger().info("config is set to not enable! Shutting down plugin...");
            getLogger().info("if this is your first time running the plugin, consider setup in config.yml");
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
