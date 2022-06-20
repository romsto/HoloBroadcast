/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.utils;

import net.mystipvp.holobroadcast.config.CustomConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class PermissionTemplate {

    private final List<Tuple<String, String>> levels = new ArrayList<>();

    public PermissionTemplate(String name, CustomConfig configc) {
        FileConfiguration config = configc.getConfig();
        int i = 1;
        while (config.get(name + "." + i) != null) {
            levels.add(Tuple.of(config.getString(name + "." + i + ".permission"), config.getString(name + "." + i + ".message")));
            i++;
        }
    }

    public String getMessage(CommandSender receiver) {
        for (int i = 0; i < levels.size(); i++) {
            if (receiver.hasPermission(levels.get(i).getFirst()))
                return levels.get(i).getSecond();
        }
        return "";
    }
}