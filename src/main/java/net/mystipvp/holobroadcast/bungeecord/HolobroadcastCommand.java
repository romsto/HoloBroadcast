/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.bungeecord;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class HolobroadcastCommand extends Command {

    public HolobroadcastCommand() {
        super("holobroadcastbungee", "holobroadcast.bungee", "hbb");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (!commandSender.hasPermission("holobroadcast.bungee")) {
            commandSender.sendMessage(TextComponent.fromLegacyText("&7[&bHolo&9Broadcast&7]&f &cYou do not have the permission to do this.".replaceAll("&", "§")));
            return;
        }

        if (strings.length == 0) {
            commandSender.sendMessage(TextComponent.fromLegacyText("&b&l&m      &3&l&m       &9&l&m        &r &b&lHolo&9&lBroadcast&r &9&l&m        &3&l&m       &b&l&m      ".replaceAll("&", "§")));
            commandSender.sendMessage(TextComponent.fromLegacyText("&7&oMade by &r&9_Rolyn &r&7&oand &r&9DevKrazy&r&7&o.          Version: &r&7bungee".replaceAll("&", "§") + HolobroadcastBungee.VERSION));
            commandSender.sendMessage(TextComponent.fromLegacyText(" "));
            commandSender.sendMessage(TextComponent.fromLegacyText("  &9/§7hbb &9<Message>".replaceAll("&", "§")));
            return;
        }

        Collection<ProxiedPlayer> networkPlayers = ProxyServer.getInstance().getPlayers();
        if (networkPlayers == null || networkPlayers.isEmpty()) {
            commandSender.sendMessage(TextComponent.fromLegacyText("&7[&bHolo&9Broadcast&7]&f &cThis command requires at least one online player!".replaceAll("&", "§")));
            return;
        }

        String message = String.join(" ", Arrays.copyOfRange(strings, 0, strings.length));

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("BroadcastHB");

        ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
        DataOutputStream msgout = new DataOutputStream(msgbytes);
        try {
            msgout.writeUTF(message);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        out.writeShort(msgbytes.toByteArray().length);
        out.write(msgbytes.toByteArray());

        ProxiedPlayer player = Iterables.getFirst(networkPlayers, null);
        assert player != null;
        player.getServer().getInfo().sendData("BungeeCord", out.toByteArray());
        commandSender.sendMessage(TextComponent.fromLegacyText("&7[&bHolo&9Broadcast&7]&f &aYou have successfully broadcast the message!".replaceAll("&", "§")));
    }
}
