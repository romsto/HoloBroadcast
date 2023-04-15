/*
 * Copyright (c) 2020-2023.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.bungeecord;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.mystipvp.holobroadcast.HoloBroadcast;
import net.mystipvp.holobroadcast.config.files.SettingsConfig;
import net.mystipvp.holobroadcast.holograms.HologramPlayersManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class BungeeChanneling implements PluginMessageListener {

    public BungeeChanneling() {
        if (SettingsConfig.isUsingBungeeMessaging()) {
            if (!Bukkit.getServer().getVersion().contains("Spigot") && !Bukkit.getServer().getVersion().contains("Paper")) {
                Bukkit.getLogger().warning("This server version isn't compatible with BungeeCord plugin messaging. If you think this is an error, please contact us on Discord!");
                return;
            }
            Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(HoloBroadcast.getInstance(), "BungeeCord");
            Bukkit.getServer().getMessenger().registerIncomingPluginChannel(HoloBroadcast.getInstance(), "BungeeCord", this);
            Bukkit.getLogger().info("Plugin connected through bungee network.");
        }
    }

    public static void sendMessage(String channel, String data) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Forward");
        out.writeUTF("ALL");
        out.writeUTF(channel);

        ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
        DataOutputStream msgout = new DataOutputStream(msgbytes);
        try {
            msgout.writeUTF(data);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        out.writeShort(msgbytes.toByteArray().length);
        out.write(msgbytes.toByteArray());

        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        assert player != null;
        player.sendPluginMessage(HoloBroadcast.getInstance(), "BungeeCord", out.toByteArray());
    }

    @Override
    public void onPluginMessageReceived(String channel, @NotNull Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subChannel = in.readUTF();
        if (subChannel.equalsIgnoreCase("BroadcastHB")) {
            short len = in.readShort();
            byte[] msgbytes = new byte[len];
            in.readFully(msgbytes);

            DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
            try {
                String msg = msgin.readUTF();
                // Broadcast message
                HologramPlayersManager manager = HologramPlayersManager.getInstance();
                Bukkit.getOnlinePlayers().forEach(o -> manager.getHologramPlayerFromUUID(o.getUniqueId()).showHUD(Bukkit.getConsoleSender(), msg, 160));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
