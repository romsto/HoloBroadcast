/*
 * Copyright (c) 2020-2023.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.utils;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.mystipvp.holobroadcast.HoloBroadcast;
import net.mystipvp.holobroadcast.holograms.HologramPlayersManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class BungeeUtil {

    /**
     * Sends the broadcast information to all other servers of the bungee
     *
     * @param sender   Player who send the message
     * @param message  Message
     * @param duration Duration
     */
    public static void broadcastToAllServers(Player sender, String message, Long duration) {
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Forward");
            out.writeUTF("ALL");
            out.writeUTF("BroadcastHologram");

            ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
            DataOutputStream msgout = new DataOutputStream(msgbytes);
            msgout.writeUTF(message);
            msgout.writeLong(duration);

            out.writeShort(msgbytes.toByteArray().length);
            out.write(msgbytes.toByteArray());

            sender.sendPluginMessage(HoloBroadcast.getInstance(), "BungeeCord", out.toByteArray());
        } catch (IOException e) {
            HoloBroadcast.getInstance().getLogger().warning("Impossible to send the broadcast packet to the other servers.");
        }
    }

    public static class MessageReceiver implements PluginMessageListener {

        @Override
        public void onPluginMessageReceived(String channel, @NotNull Player player, byte[] bytes) {
            if (!channel.equals("BungeeCord")) {
                return;
            }

            ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
            String subChannel = in.readUTF();
            if (subChannel.equals("BroadcastHologram")) {
                short len = in.readShort();
                byte[] msgbytes = new byte[len];
                in.readFully(msgbytes);

                DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
                try {
                    String message = msgin.readUTF();
                    long duration = msgin.readLong();
                    // Broadcast message
                    HologramPlayersManager manager = HologramPlayersManager.getInstance();
                    Bukkit.getOnlinePlayers().forEach(o -> manager.getHologramPlayerFromUUID(o.getUniqueId()).showHUD(Bukkit.getConsoleSender(), message, duration));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
