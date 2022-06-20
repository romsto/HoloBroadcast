/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.commands;

import net.mystipvp.holobroadcast.bungeecord.BungeeChanneling;
import net.mystipvp.holobroadcast.config.files.TemplatesConfig;
import net.mystipvp.holobroadcast.events.BroadcastHUDEvent;
import net.mystipvp.holobroadcast.holograms.HologramPlayersManager;
import net.mystipvp.holobroadcast.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class BroadcastAllSubCommand extends SubCommand {

    public BroadcastAllSubCommand() {
        super("broadcastall", "Send an hologram message to all the servers on bungeecord.", "<Message>", 1);
        setRequiredPermission("holobroadcast.broadcastmessageall");
        addAlias("bcall");
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        String message = String.join(" ", Arrays.copyOfRange(args, 0, args.length));

        for (String s : TemplatesConfig.getTemplatesList()) {
            if (message.contains("%template_" + s + "%"))
                message = message.replaceAll("%template_" + s + "%", TemplatesConfig.getTemplate(s));
        }

        if (!(sender instanceof Player)) {
            Message.CONSOLE_BUNGEE_NOT.send(sender);
            return true;
        }

        Message.SUCCESSFULLY_BROADCAST.send(sender);
        final String msg = message;
        // Broadcast message
        BungeeChanneling.sendMessage("BroadcastHB", msg);

        BroadcastHUDEvent event = new BroadcastHUDEvent(sender, msg, 160);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            HologramPlayersManager manager = HologramPlayersManager.getInstance();
            Bukkit.getOnlinePlayers().forEach(o -> manager.getHologramPlayerFromUUID(o.getUniqueId()).showHUD(sender, event.getMessage(), event.getDuration()));
        }
        return true;
    }
}
