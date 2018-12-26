package fi.matiaspaavilainen.masuitecore;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class MaSuiteCore extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        super.onEnable();
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginMessageListener(this));
        getServer().getPluginManager().registerEvents(this, this);

    }

}
