package fi.matiaspaavilainen.masuitecore;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class MaSuiteCore extends JavaPlugin implements Listener {

    private static Chat chat = null;

    @Override
    public void onEnable() {
        super.onEnable();
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginMessageListener(this));
        this.getServer().getPluginManager().registerEvents(this, this);
        setupChat();
    }

    @EventHandler
    public void onJoin(PlayerLoginEvent e){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("MaSuitePlayerGroup");
        out.writeUTF(e.getPlayer().getUniqueId().toString());
        out.writeUTF(getPrefix(e.getPlayer()));
        out.writeUTF(getSuffix(e.getPlayer()));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        getServer().sendPluginMessage(this, "BungeeCord", out.toByteArray());
        System.out.println("asddaw");
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }

        return (chat != null);
    }

    static Chat getChat() {
        return chat;
    }

    String getPrefix(Player p) {
        if (getChat() != null) {
            if (getChat().getPlayerPrefix(p) != null) {
                return getChat().getPlayerPrefix(p);
            } else if (getChat().getGroupPrefix(p.getWorld(), getChat().getPrimaryGroup(p)) != null) {
                return getChat().getGroupPrefix(p.getWorld(), getChat().getPrimaryGroup(p));
            }
            return "";
        }
        return "";
    }

    String getSuffix(Player p) {
        if (getChat() != null) {
            if (getChat().getPlayerSuffix(p) != null) {
                return getChat().getPlayerSuffix(p);
            } else if (getChat().getGroupSuffix(p.getWorld(), getChat().getPrimaryGroup(p)) != null) {
                return getChat().getGroupSuffix(p.getWorld(), getChat().getPrimaryGroup(p));
            }
            return "";
        }
        return "";
    }
}
