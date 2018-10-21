package fi.matiaspaavilainen.masuitecore;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class PluginMessageListener implements org.bukkit.plugin.messaging.PluginMessageListener {
    private static MaSuiteCore plugin;

    PluginMessageListener(MaSuiteCore p) {
        plugin = p;
    }

    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));

        String subchannel = null;
        try {
            subchannel = in.readUTF();
            if (subchannel.equals("MaSuitePlayerLocation")) {
                Player p = Bukkit.getPlayer(UUID.fromString(in.readUTF()));
                if(p == null){
                    return;
                }
                Location loc = p.getLocation();
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("MaSuitePlayerLocation");
                out.writeUTF(String.valueOf(p.getUniqueId()));
                out.writeUTF(loc.getWorld().getName());
                out.writeDouble(loc.getX());
                out.writeDouble(loc.getY());
                out.writeDouble(loc.getZ());
                out.writeFloat(loc.getYaw());
                out.writeFloat(loc.getPitch());
                player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
            }
            if (subchannel.equals("MaSuitePlayerGroup")) {
                Player p = Bukkit.getPlayer(UUID.fromString(in.readUTF()));
                if(p == null){
                    return;
                }
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("MaSuitePlayerGroup");
                out.writeUTF(String.valueOf(p.getUniqueId()));
                out.writeUTF(getPrefix(p));
                out.writeUTF(getSuffix(p));
                player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private String getPrefix(Player p) {
        if (plugin.getChat() != null) {
            if (plugin.getChat().getPlayerPrefix(p) != null) {
                return plugin.getChat().getPlayerPrefix(p);
            } else if (plugin.getChat().getGroupPrefix(p.getWorld(), plugin.getChat().getPrimaryGroup(p)) != null) {
                return plugin.getChat().getGroupPrefix(p.getWorld(), plugin.getChat().getPrimaryGroup(p));
            }
            return "";
        }
        return "";
    }

    private String getSuffix(Player p) {
        if (plugin.getChat() != null) {
            if (plugin.getChat().getPlayerSuffix(p) != null) {
                return plugin.getChat().getPlayerSuffix(p);
            } else if (plugin.getChat().getGroupSuffix(p.getWorld(), plugin.getChat().getPrimaryGroup(p)) != null) {
                return plugin.getChat().getGroupSuffix(p.getWorld(), plugin.getChat().getPrimaryGroup(p));
            }
            return "";
        }
        return "";
    }
}
