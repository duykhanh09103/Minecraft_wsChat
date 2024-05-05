package github.duykhanh09103;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;


public class PlayerListener implements Listener {
    minecraft_wschat plugin = (minecraft_wschat) Bukkit.getServer().getPluginManager().getPlugin("minecraft_wschat");
    wsClient client;

    {
        assert plugin != null;
        client = plugin.client;
    }

    @EventHandler
    public void onPlayerChat(@NotNull AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        client.send("[minecraft] "+player.getName()+" : "+message);
        //Bukkit.broadcastMessage("[facebook]"+player.getName()+message);

    }
};

