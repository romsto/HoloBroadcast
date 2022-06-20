/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.commands;

import net.mystipvp.holobroadcast.config.files.SettingsConfig;
import net.mystipvp.holobroadcast.holograms.HologramPlayer;
import net.mystipvp.holobroadcast.holograms.HologramPlayersManager;
import net.mystipvp.holobroadcast.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class MessageSubCommand extends SubCommand {

    public MessageSubCommand() {
        super("msg", "Send a private message to an online player.", "<Player> <Message>", 2);
        setRequiredPermission("holobroadcast.message");
        addAlias("message");
        addAlias("dm");
        addAlias("direct");
        addAlias("pv");
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        String targetName = args[0];
        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            Message.PLAYER_OFFLINE.send(sender);
            return true;
        }

        String template = SettingsConfig.getPMMessages();

        template = template.replaceAll("%message%", String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
        template = template.replaceAll("%sender%", sender.getName());

        // Send message
        HologramPlayersManager manager = HologramPlayersManager.getInstance();
        HologramPlayer hplayer = manager.getHologramPlayerFromUUID(target.getUniqueId());
        hplayer.showHUD(sender, template, SettingsConfig.getPMDuration());
        if (sender instanceof Player) {
            hplayer.lastMessage = sender.getName();
            manager.getHologramPlayerFromUUID(((Player) sender).getUniqueId()).lastMessage = targetName;
        }
        Message.MESSAGE_SENT.send(sender);

        return true;
    }

}
