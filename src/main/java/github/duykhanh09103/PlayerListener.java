package github.duykhanh09103;


//import org.bukkit.Bukkit; unused

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.advancement.Advancement;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
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
        boolean enable = config.getBoolean("listen.onPlayerChat");
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
      boolean enable = config.getBoolean("listen.onPlayerJoin");
        Player player = event.getPlayer();
        if(client != null&&client.isOpen()&&enable){

            client.send("[minecraft] "+player.getName()+" has joined the game");
        }
        boolean statusInTab = config.getBoolean("statusInTab");
        if(statusInTab){
            if (!client.isOpen()){
                player.setPlayerListFooter(ChatColor.translateAlternateColorCodes('&',"&fWebsocket status&r: not initialized!"));
            }
            player.setPlayerListFooter(ChatColor.translateAlternateColorCodes('&',"&fWebsocket status&r: &2Connected &r"));
        }
    }
    //send ws on player leave
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        final wsClient client = minecraft_wschat.client;
        FileConfiguration config = plugin.getConfig();
        boolean enable = config.getBoolean("listen.onPlayerQuit");
        if(client != null&&client.isOpen()&&enable){
            Player player = event.getPlayer();
            client.send("[minecraft] "+player.getName()+" has left the game");
        }
    }
    //send ws when player complete advancement
    @EventHandler
    public void onPlayerAdvancementDone( PlayerAdvancementDoneEvent event){
        Player player = event.getPlayer();
        Advancement advancement = event.getAdvancement();
        final wsClient client = minecraft_wschat.client;
        FileConfiguration config = plugin.getConfig();
        boolean enable = config.getBoolean("listen.onAdvancementDone");
        var advancementtypemessage=switch(Objects.requireNonNull(advancement.getDisplay()).getType().toString().toUpperCase()){
            default ->  "Had reach something i dont fucking know" ;
            case "CHALLENGE" -> "has complete a challenge";
            case "GOAL" -> "has reached the goal";
            case "TASK" -> "has made the advancement";
        };
        if( advancement.getKey().toString().contains("minecraft/recipe")) {
            return;
        }
        else if(client!= null&&client.isOpen()&&enable&& !Objects.requireNonNull(advancement.getDisplay()).isHidden()){
         client.send("[minecraft] "+player.getName()+" "+advancementtypemessage+" "+ advancement.getDisplay().getTitle() );
        }

    }

    //send ws on player dead
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        final wsClient client = minecraft_wschat.client;
        FileConfiguration config = plugin.getConfig();
        boolean enable = config.getBoolean("listen.onPlayerDeath");
        if(client != null&&client.isOpen()&&enable){
            client.send("[minecraft] "+event.getDeathMessage());
        }
    }


}

