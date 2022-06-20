/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.commands;

import net.mystipvp.holobroadcast.config.files.TemplatesConfig;
import net.mystipvp.holobroadcast.holograms.HologramPlayersManager;
import net.mystipvp.holobroadcast.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedList;

public class SendTemplateSubCommand extends SubCommand {

    public SendTemplateSubCommand() {
        super("sendtemplate", "Send an hologram message to an online player, based on a template message.", "<Player> <Template Name> [Arguments...]", 2);
        setRequiredPermission("holobroadcast.sendtemplate");
        addAlias("template");
        addAlias("temp");
        addAlias("showtemplate");
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        String targetName = args[0];
        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            Message.PLAYER_OFFLINE.send(sender);
            return true;
        }

        String template = TemplatesConfig.getTemplate(args[1]);
        if (template.equals("notfound.")) {
            Message.TEMPLATE_NOT_FOUND.send(sender);
            return true;
        }

        if (args.length > 2) {
            template = template.replaceAll("%argument_all%", String.join(" ", Arrays.copyOfRange(args, 2, args.length)));

            LinkedList<String> arguments = new LinkedList<>();

            for (int i = 2; i < args.length; i++)
                arguments.push(args[i]);

            for (int i = 2; i < args.length; i++) {
                if (template.contains("%argument_" + (i - 1) + "%") && !arguments.isEmpty())
                    template = template.replaceAll("%argument_" + (i - 1) + "%", arguments.pollLast());
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

        // Send message
        HologramPlayersManager manager = HologramPlayersManager.getInstance();
        manager.getHologramPlayerFromUUID(target.getUniqueId()).showHUD(sender, template, -1);
        Message.MESSAGE_SENT.send(sender);

        return true;
    }

}
