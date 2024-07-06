package github.duykhanh09103;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;

public class wsClient  extends WebSocketClient  {
    private  final JavaPlugin plugin;
    private  int retryAttempt = 0;

    public wsClient(URI serverUri, JavaPlugin plugin) {
        super(serverUri,new Draft_6455(),null,5000);
    this.plugin = plugin ;
    }

    public void changeAllPlayerTablist(@Nullable String str1 , @Nullable String str2){
        for(Player player : Bukkit.getOnlinePlayers()){
            player.setPlayerListHeaderFooter(str1,str2);
        };
    }
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Bukkit.getServer().getLogger().info("[Minecraft_wsChat] Connection to websocket server success!");
        changeAllPlayerTablist(null,ChatColor.translateAlternateColorCodes('&',"&fWebsocket status&r: &2Connected &r"));
    retryAttempt=0;
    }

    @Override
    public void onMessage(String message) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',message));
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Bukkit.getServer().getLogger().info("[Minecraft_wsChat] Connection to websocket server closed!");
        changeAllPlayerTablist(null,ChatColor.translateAlternateColorCodes('&',"&fWebsocket status&r: &4Disconnected &r"));
    if(remote){
        tryToReconnect();
    }
    }

    @Override
    public void onError(@NotNull Exception ex) {
        ex.printStackTrace();
    }

    private void tryToReconnect(){
        int maxRetryAttempt = plugin.getConfig().getInt("maxRetryAttempt");
        if (maxRetryAttempt != 0 ){
            if(retryAttempt<maxRetryAttempt){
            retryAttempt++;
            Bukkit.getServer().getLogger().info("[Minecraft_wsChat] Attempting to reconnect... (" + retryAttempt + "/" + maxRetryAttempt + ")");
            Bukkit.getScheduler().runTaskLater(plugin,this::reconnectSafely,100L);
            }
            else{
                Bukkit.getServer().getLogger().info("[Minecraft_wsChat] Max retry attempts reached. Not gonna reconnecting. Please reconnect manually with /reconnect command.");
            }
        }
        else{
            Bukkit.getServer().getLogger().info("[Minecraft_wsChat] maxRetryAttempt is 0 or undefined!Not gonna reconnect automatically! If this is not intended, please change in config.yml");
        }
    }
    private void reconnectSafely() {
        try {
            this.reconnect();
        } catch (IllegalStateException e) {
            Bukkit.getServer().getLogger().warning("[Minecraft_wsChat] Reconnect attempt failed: " + e.getMessage());
            tryToReconnect();
        }
    }
}
