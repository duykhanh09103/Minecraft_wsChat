package github.duykhanh09103;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URI;
import java.net.URISyntaxException;

public final class minecraft_wschat extends JavaPlugin {
    public static wsClient client;
    FileConfiguration config = getConfig();
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getCommand("reconnect").setExecutor(new wsClientReconnect());
        config.addDefault("Uri", "ws://localhost:8080");
        config.options().copyDefaults(true);
        saveConfig();
        try {
            client = new wsClient(new URI(config.getString("Uri")));
            client.setConnectionLostTimeout(30);
            client.connectBlocking();

        } catch (URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }
        Bukkit.getScheduler().runTaskLater(this,() ->{
            if(client != null&&client.isOpen()){
            client.send("Server is on!");}
        },100L);
    }

    @Override
    public void onDisable() {
        if(client != null&&client.isOpen()){
            client.send("Server stopped!");
            client.close();
        }
    }

}
