package github.duykhanh09103;

import org.bukkit.plugin.java.JavaPlugin;

import java.net.URI;
import java.net.URISyntaxException;

public final class minecraft_wschat extends JavaPlugin {
    public wsClient client;
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        try {
            client = new wsClient(new URI("ws://localhost:8080")) {
            };
            client.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDisable() {
        if(client != null&&client.isOpen()){
            client.close();
        }
    }

}
