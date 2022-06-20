/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.commands;

import net.mystipvp.holobroadcast.config.files.TemplatesConfig;
import net.mystipvp.holobroadcast.events.BroadcastHUDEvent;
import net.mystipvp.holobroadcast.holograms.HologramPlayersManager;
import net.mystipvp.holobroadcast.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.LinkedList;

public class BroadcastTemplateSubCommand extends SubCommand {

    public BroadcastTemplateSubCommand() {
        super("broadcasttemplate", "Send an hologram message to all the online player, based on a template message.", "<Template Name> [Arguments...]", 1);
        setRequiredPermission("holobroadcast.broadcasttemplate");
        addAlias("bctemplate");
        addAlias("bctemp");
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        String template = TemplatesConfig.getTemplate(args[0]);
        if (template.equals("notfound.")) {
            Message.TEMPLATE_NOT_FOUND.send(sender);
            return true;
        }

        if (args.length > 1) {
            template = template.replaceAll("%argument_all%", String.join(" ", Arrays.copyOfRange(args, 1, args.length)));

            LinkedList<String> arguments = new LinkedList<>();

            for (int i = 1; i < args.length; i++)
                arguments.push(args[i]);

            for (int i = 1; i < args.length; i++) {
                System.out.println(arguments.get(i - 1));
                if (template.contains("%argument_" + i + "%") && !arguments.isEmpty())
                    template = template.replaceAll("%argument_" + i + "%", arguments.pollLast());
                else
                    break;
            }

            if (!arguments.isEmpty() && template.contains("%argument_other%")) {
                StringBuilder builder = new StringBuilder(arguments.pollLast());
                while (!arguments.isEmpty())
                    builder.append(" ").append(arguments.pollLast());
                template = template.replaceAll("%argument_other%", builder.toString());
            }
        }

        /*if (!(sender instanceof Player))
            Message.CONSOLE_BUNGEE_NOT.send(sender);
        else
            BungeeUtil.broadcastToAllServers((Player) sender, message, 160L);*/
        final String message = template;
        Message.SUCCESSFULLY_BROADCAST.send(sender);
        // Broadcast message
        BroadcastHUDEvent event = new BroadcastHUDEvent(sender, message, 160);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            HologramPlayersManager manager = HologramPlayersManager.getInstance();
            Bukkit.getOnlinePlayers().forEach(o -> manager.getHologramPlayerFromUUID(o.getUniqueId()).showHUD(sender, event.getMessage(), event.getDuration()));
        }
        return true;
    }
}
