package github.duykhanh09103;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class wsClientReconnect implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        wsClient client =minecraft_wschat.client;
        client.reconnect();
        Bukkit.getServer().getLogger().info("[Minecraft_wsChat] Reconnecting...");
        return true;
    }
}
