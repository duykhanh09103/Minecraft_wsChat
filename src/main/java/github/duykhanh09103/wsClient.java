package github.duykhanh09103;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import org.bukkit.plugin.java.JavaPlugin;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.NotNull;

public class wsClient extends WebSocketClient {
    public wsClient(URI serverUri) {
        super(serverUri);
    }
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to server");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received message: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed");
    }

    @Override
    public void onError(@NotNull Exception ex) {
        ex.printStackTrace();
    }

}
