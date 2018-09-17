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

import static fi.matiaspaavilainen.masuitecore.MaSuiteCore.getChat;

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

    private static String getPrefix(Player p) {
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

    private static String getSuffix(Player p) {
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
