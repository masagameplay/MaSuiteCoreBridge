package fi.matiaspaavilainen.masuitecore;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class MaSuiteCore extends JavaPlugin implements Listener {

    private static Chat chat = null;

    @Override
    public void onEnable() {
        super.onEnable();
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginMessageListener(this));
        getServer().getPluginManager().registerEvents(this, this);
        setupChat();

        if(chat == null){
            getServer().getPluginManager().disablePlugin(this);
            System.out.println("[MaSuite] [Core] Vault not found... Disabling...");
        }
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }

        return (chat != null);
    }

    public Chat getChat() {
        return chat;
    }
}
