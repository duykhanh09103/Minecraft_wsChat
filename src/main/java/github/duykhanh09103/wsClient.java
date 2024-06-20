package github.duykhanh09103;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;

public class wsClient  extends WebSocketClient  {
    public wsClient(URI serverUri) {
        super(serverUri,new Draft_6455(),null,5000);
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
    }

    @Override
    public void onMessage(String message) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',message));
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Bukkit.getServer().getLogger().info("[Minecraft_wsChat] Connection to websocket server closed!");
        changeAllPlayerTablist(null,ChatColor.translateAlternateColorCodes('&',"&fWebsocket status&r: &4Disconnected &r"));
    }

    @Override
    public void onError(@NotNull Exception ex) {
        ex.printStackTrace();
    }
}