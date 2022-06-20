/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.commands;

import net.mystipvp.holobroadcast.holograms.HologramPlayersManager;
import net.mystipvp.holobroadcast.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearSubCommand extends SubCommand {

    public ClearSubCommand() {
        super("clear", "Clear your current message hologram.", "", 0);
        this.setRequiredPermission("holobroadcast.clear");
        addAlias("rmv");
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        HologramPlayersManager manager = HologramPlayersManager.getInstance();
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                Message.CONSOLE_NOT_SUPPORTED.send(sender);
                return true;
            }
            // Clear message
            manager.getHologramPlayerFromUUID(((Player) sender).getUniqueId()).deleteHUD();
            Message.MESSAGE_CLEARED.send(sender);
            return true;
        } else {
            if (!sender.hasPermission("holobroadcast.clear.other")) {
                Message.PERMISSION_DENIED.send(sender);
                return true;
            }

            String targetName = args[0];
            Player target = Bukkit.getPlayer(targetName);
            if (target != null) {
                // Clear message
                manager.getHologramPlayerFromUUID(target.getUniqueId()).deleteHUD();
                Message.MESSAGE_CLEARED.send(sender);
            } else {
                Message.PLAYER_OFFLINE.send(sender);
            }
            return true;
        }
    }

}
