package github.duykhanh09103;


//import org.bukkit.Bukkit; unused
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;
//import org.bukkit.plugin.java.JavaPlugin; unused

public class    PlayerListener implements Listener {
    @EventHandler
    public void onPlayerChat(@NotNull AsyncPlayerChatEvent event) {
        final wsClient client = minecraft_wschat.client;
        if(client != null&&client.isOpen()){
            Player player = event.getPlayer();
            String message = event.getMessage();
            client.send("[minecraft] "+player.getName()+" : "+message);
        }
        //Bukkit.broadcastMessage("[facebook]"+player.getName()+message);

    }
}

