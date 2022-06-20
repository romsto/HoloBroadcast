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

public class SendSubCommand extends SubCommand {

    public SendSubCommand() {
        super("send", "Send an hologram message to an online player.", "<Player> <Message>", 2);
        setRequiredPermission("holobroadcast.sendmessage");
        addAlias("display");
        addAlias("show");
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        String targetName = args[0];
        Player target = Bukkit.getPlayer(targetName);
        if (target != null) {
            // Send message
            HologramPlayersManager manager = HologramPlayersManager.getInstance();
            String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            for (String s : TemplatesConfig.getTemplatesList()) {
                if (message.contains("%template_" + s + "%"))
                    message = message.replaceAll("%template_" + s + "%", TemplatesConfig.getTemplate(s));
            }
            final String msg = message;
            manager.getHologramPlayerFromUUID(target.getUniqueId()).showHUD(sender, msg, -1);
            Message.MESSAGE_SENT.send(sender);
        } else {
            Message.PLAYER_OFFLINE.send(sender);
        }
        return true;
    }

}
