package github.duykhanh09103;


//import org.bukkit.Bukkit; unused
import org.bukkit.Bukkit;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
//import org.bukkit.plugin.java.JavaPlugin; unused

public class PlayerListener implements Listener {
    //initialize main plugin(this took me days tf)
    private final minecraft_wschat plugin ;

    public PlayerListener(minecraft_wschat plugin) {
        this.plugin = plugin;
    }

    //send ws on player chat
    @EventHandler
    public void onPlayerChat(@NotNull AsyncPlayerChatEvent event) {
        final wsClient client = minecraft_wschat.client;
        FileConfiguration  config = plugin.getConfig();
        boolean enable = config.getBoolean("ListenOnPlayerChat");
        if(client != null&&client.isOpen()&&enable) {
            Player player = event.getPlayer();
            String message = event.getMessage();
            client.send("[minecraft] " + player.getName() + " : " + message);
        }

    }
    //send ws on player join
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        final wsClient client = minecraft_wschat.client;
      FileConfiguration config = plugin.getConfig();
      boolean enable = config.getBoolean("ListenOnPlayerJoin");
        if(client != null&&client.isOpen()&&enable){
            Player player = event.getPlayer();
            client.send("[minecraft] "+player.getName()+" has joined the game");
        }
    }
    //send ws on player leave
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        final wsClient client = minecraft_wschat.client;
        FileConfiguration config = plugin.getConfig();
        boolean enable = config.getBoolean("ListenOnPlayerQuit");
        if(client != null&&client.isOpen()&&enable){
            Player player = event.getPlayer();
            client.send("[minecraft] "+player.getName()+" has left the game");
        }
    }

}

