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

public class ReplySubCommand extends SubCommand {

    public ReplySubCommand() {
        super("reply", "Reply to a previous private message.", "<Message>", 1);
        setRequiredPermission("holobroadcast.reply");
        addAlias("r");
        addAlias("answer");
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        HologramPlayersManager manager = HologramPlayersManager.getInstance();

        if (!(sender instanceof Player)) {
            Message.CONSOLE_BUNGEE_NOT.send(sender);
            return true;
        }

        HologramPlayer p = manager.getHologramPlayerFromUUID(((Player) sender).getUniqueId());
        Player target = Bukkit.getPlayer(p.lastMessage);
        if (target == null) {
            Message.PLAYER_OFFLINE.send(sender);
            return true;
        }

        String template = SettingsConfig.getPMMessages();

        template = template.replaceAll("%message%", String.join(" ", Arrays.copyOfRange(args, 0, args.length)));
        template = template.replaceAll("%sender%", sender.getName());

        Message.MESSAGE_SENT.send(sender);
        final String msg = template;
        Bukkit.getOnlinePlayers().forEach(o -> manager.getHologramPlayerFromUUID(o.getUniqueId()).showHUD(sender, msg, SettingsConfig.getPMDuration()));
        return true;
    }
}
