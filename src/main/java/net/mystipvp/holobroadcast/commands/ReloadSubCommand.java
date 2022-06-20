/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.commands;

import net.mystipvp.holobroadcast.HoloBroadcast;
import net.mystipvp.holobroadcast.config.files.AnimationsConfig;
import net.mystipvp.holobroadcast.config.files.PermissionTemplatesConfig;
import net.mystipvp.holobroadcast.config.files.SchedulerConfig;
import net.mystipvp.holobroadcast.config.files.TemplatesConfig;
import net.mystipvp.holobroadcast.utils.AutoAnnouncer;
import net.mystipvp.holobroadcast.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ReloadSubCommand extends SubCommand {

    public ReloadSubCommand() {
        super("reload", "Reloads HoloBroadcast's config files.", "", 0);
        setRequiredPermission("holobroadcast.reload");
        addAlias("rl");
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        HoloBroadcast.getSettingsConfig().reload();
        HoloBroadcast.getMessagesConfig().reload();
        HoloBroadcast.getPlayersDataConfig().reload();
        HoloBroadcast.getAnimationsConfig().reload();
        HoloBroadcast.getSchedulerConfig().reload();
        HoloBroadcast.getTemplatesConfig().reload();
        AnimationsConfig.refetch();
        TemplatesConfig.refetch();
        PermissionTemplatesConfig.refetch();
        SchedulerConfig.refetch();
        AutoAnnouncer.update();

        Message.RELOAD_SUCCESS.send(sender);
        return true;
    }
}
