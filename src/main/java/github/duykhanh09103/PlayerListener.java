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
        FileConfiguration config = plugin.getConfig();
        FileConfiguration messagesConfig = plugin.getMessagesConfig();
        boolean enable = config.getBoolean("listen.onPlayerChat");
        String messages = messagesConfig.getString("onPlayerChat");
        if(client != null&&client.isOpen()&&enable) {
            Player player = event.getPlayer();
            String message = event.getMessage();
            if(messages != null&& !messages.isEmpty()) {
                client.send(messages.replace("%playerName%",player.getName()).replace("%playerMessage%",message));
            }
            else {
                Bukkit.getServer().getLogger().info(ChatColor.YELLOW + "[Minecraft_wsChat]: Warning! messages.yaml onPlayerChat event is null! using default message ");
                client.send("[minecraft] " + player.getName() + " : " + message);
            }
        }

    }
    //send ws on player join
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        final wsClient client = minecraft_wschat.client;
      FileConfiguration config = plugin.getConfig();
      FileConfiguration messagesConfig = plugin.getMessagesConfig();
      boolean enable = config.getBoolean("listen.onPlayerJoin");
        String messages = messagesConfig.getString("onPlayerJoin");
        Player player = event.getPlayer();
        if(client != null&&client.isOpen()&&enable){
            if(messages != null&& !messages.isEmpty()){
                client.send(messages.replace("%playerName%",player.getName()));
            }
            else {
                Bukkit.getServer().getLogger().info(ChatColor.YELLOW + "[Minecraft_wsChat]: Warning! messages.yaml onPlayerJoin event is null! using default message ");
                client.send("[minecraft] " + player.getName() + " has joined the game");

            }
        }
        boolean statusInTab = config.getBoolean("statusInTab");
        if(statusInTab){
            if(client != null&&client.isOpen()){
                player.setPlayerListFooter(ChatColor.translateAlternateColorCodes('&',"&fWebsocket status&r: &2Connected &r"));
            }
            else{
                player.setPlayerListFooter(ChatColor.translateAlternateColorCodes('&',"&fWebsocket status&r: not initialized!"));
            }
        }
    }
    //send ws on player leave
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        final wsClient client = minecraft_wschat.client;
        FileConfiguration config = plugin.getConfig();
        FileConfiguration messagesConfig = plugin.getMessagesConfig();
        boolean enable = config.getBoolean("listen.onPlayerQuit");
        String messages = messagesConfig.getString("onPlayerQuit");
        if(client != null&&client.isOpen()&&enable){
            Player player = event.getPlayer();
            if(messages != null&& !messages.isEmpty()){
                client.send(messages.replace("%playerName%",player.getName()));
            }
            else {
                Bukkit.getServer().getLogger().info(ChatColor.YELLOW + "[Minecraft_wsChat]: Warning! messages.yaml onPlayerQuit event is null! using default message ");
                client.send("[minecraft] " + player.getName() + " has left the game");
            }
        }
    }
    //send ws when player complete advancement
    @EventHandler
    public void onPlayerAdvancementDone( PlayerAdvancementDoneEvent event){
        Player player = event.getPlayer();
        Advancement advancement = event.getAdvancement();
        final wsClient client = minecraft_wschat.client;
        FileConfiguration config = plugin.getConfig();
        FileConfiguration messagesConfig = plugin.getMessagesConfig();
        boolean enable = config.getBoolean("listen.onAdvancementDone");
        String messages = messagesConfig.getString("onPlayerAdvancementDone");
        if (advancement.getDisplay() == null) {
            return;
        }
        var advancementTypeMessage=switch(Objects.requireNonNull(advancement.getDisplay()).getType().toString().toUpperCase()){
            default ->  "Had reach something i dont fucking know" ;
            case "CHALLENGE" -> "has complete a challenge";
            case "GOAL" -> "has reached the goal";
            case "TASK" -> "has made the advancement";
        };
        if( advancement.getKey().toString().contains("minecraft/recipe")) {
            return;
        }
        else if(client!= null&&client.isOpen()&&enable&& !Objects.requireNonNull(advancement.getDisplay()).isHidden()){
            if(messages != null&& !messages.isEmpty()){
                client.send(messages.replace("%playerName%",player.getName()).replace("%advancementTypeMessage%",advancementTypeMessage).replace("%advancementTitle%",advancement.getDisplay().getTitle()));
            }
            else{
                Bukkit.getServer().getLogger().info(ChatColor.YELLOW + "[Minecraft_wsChat]: Warning! messages.yaml onPlayerAdvancementDone event is null! using default message ");
                client.send("[minecraft] "+player.getName()+" "+advancementTypeMessage+" "+ advancement.getDisplay().getTitle() );
            }
        }

    }

    //send ws on player dead
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        final wsClient client = minecraft_wschat.client;
        Player player = event.getEntity().getPlayer();
        FileConfiguration config = plugin.getConfig();
        FileConfiguration messagesConfig = plugin.getMessagesConfig();
        boolean enable = config.getBoolean("listen.onPlayerDeath");
        String messages = messagesConfig.getString("onPlayerDeath");
        if(client != null&&client.isOpen()&&enable){
            if(messages != null&& !messages.isEmpty()){
                client.send(messages.replace("%playerName%",player.getName()).replace("%playerDeathMessage%",event.getDeathMessage()));
            }
            else{
                Bukkit.getServer().getLogger().info(ChatColor.YELLOW + "[Minecraft_wsChat]: Warning! messages.yaml onPlayerDeath event is null! using default message ");
                client.send("[minecraft] "+event.getDeathMessage());
            }
        }
    }


}

