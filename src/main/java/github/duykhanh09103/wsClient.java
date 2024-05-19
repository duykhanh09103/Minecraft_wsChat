package github.duykhanh09103;

import java.net.URI;
//import java.net.URISyntaxException;  unused
//import java.nio.ByteBuffer; unused

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import org.java_websocket.client.WebSocketClient;
//import org.java_websocket.drafts.Draft;  unused
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.NotNull;

public class wsClient  extends WebSocketClient  {
    public wsClient(URI serverUri) {
        super(serverUri);
    }
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Bukkit.getServer().getLogger().info("[Minecraft_wsChat] Connection to websocket server success!");

    }

    @Override
    public void onMessage(String message) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',message));
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Bukkit.getServer().getLogger().info("[Minecraft_wsChat] Connection to websocket server closed!");
    }

    @Override
    public void onError(@NotNull Exception ex) {
        ex.printStackTrace();
    }
}
