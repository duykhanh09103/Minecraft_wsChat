package github.duykhanh09103;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public final class minecraft_wschat extends JavaPlugin {
    public static wsClient client;
    FileConfiguration config = getConfig();
    private File messageFile = new File(getDataFolder(),"messages.yml");
    private FileConfiguration messagesConfig = YamlConfiguration.loadConfiguration(messageFile);
    @Override
    public void onEnable() {
        if(!messageFile.exists()){
            saveResource("messages.yml",false);
        };
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        this.getCommand("reconnect").setExecutor(new wsClientReconnect());
        config.addDefault("Uri", "ws://localhost:8080");
        config.addDefault("statusInTab",true);
        config.addDefault("listen.enable",false);
        config.addDefault("listen.onPlayerChat", true);
        config.addDefault("listen.onPlayerJoin", true);
        config.addDefault("listen.onPlayerQuit", true);
        config.addDefault("listen.onAdvancementDone",true);
        config.addDefault("listen.onPlayerDeath",true);
        config.addDefault("listen.sendOnServerDisableAndEnable",false);
        config.addDefault("maxRetryAttempt",5);
        config.options().copyDefaults(true);
        saveConfig();
        if (config.getBoolean("listen.enable")) {
            try {
                client = new wsClient(new URI(Objects.requireNonNull(config.getString("Uri"))),this);
                client.setConnectionLostTimeout(5);
                client.connectBlocking();

            } catch (URISyntaxException | InterruptedException e) {
                e.printStackTrace();
            }
            if(config.getBoolean("listen.sendOnServerDisableAndEnable")){
            Bukkit.getScheduler().runTaskLater(this, () -> {
                if (client != null && client.isOpen()) {
                    String messages = messagesConfig.getString("serverOn");
                    if(messages == null||messages.isEmpty()) {
                        Bukkit.getServer().getLogger().info(ChatColor.YELLOW + "[Minecraft_wsChat]: Warning! messages.yaml serverOn event is null! using default message ");
                        client.send("Server is on!");
                    }
                    else{
                        client.send(messages);
                    }
                }
            }, 20L);}
        }
        if(!config.getBoolean("listen.enable")){
            getLogger().info("config is set to not enable! Shutting down plugin...");
            getLogger().info("if this is your first time running the plugin, consider setup in config.yml");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        if(config.getBoolean("listen.sendOnServerDisableAndEnable")){
        if (client != null && client.isOpen()) {
            String messages = messagesConfig.getString("serverOff");
            if(messages == null|| messages.isEmpty()){
                Bukkit.getServer().getLogger().info(ChatColor.YELLOW + "[Minecraft_wsChat]: Warning! messages.yaml serverOff event is null! using default message ");
                client.send("Server stopped!");
            }
            else{
                client.send(messages);
            }
            client.close();
        }
       }
    }
    public FileConfiguration getMessagesConfig(){
        return messagesConfig;
    }
}
