/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.commands;

import net.mystipvp.holobroadcast.guis.PlayerSettingsGUI;
import net.mystipvp.holobroadcast.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SettingsSubCommand extends SubCommand {

    public SettingsSubCommand() {
        super("settings", "Change your personal settings about HUD.", "", 0);
        this.setRequiredPermission("holobroadcast.settings");
        addAlias("parameters");
        addAlias("param");
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Message.CONSOLE_NOT_SUPPORTED.send(sender);
            return true;
        }
        // Clear message
        PlayerSettingsGUI.open((Player) sender);
        return true;
    }

}
