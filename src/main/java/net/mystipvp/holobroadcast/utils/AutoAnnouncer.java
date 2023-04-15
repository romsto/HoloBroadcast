/*
 * Copyright (c) 2020-2023.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.utils;

import net.mystipvp.holobroadcast.HoloBroadcast;
import net.mystipvp.holobroadcast.config.files.SettingsConfig;
import net.mystipvp.holobroadcast.holograms.HologramPlayer;
import net.mystipvp.holobroadcast.holograms.HologramPlayersManager;
import org.bukkit.Bukkit;

import java.util.List;

public class AutoAnnouncer {

    static int taskId = 0;
    static List<String> messages;

    private static int cursor;
    private static long duration, period;

    public static void update() {
        messages = SettingsConfig.getAutoAnnouncerMessages();
        cursor = 0;
        duration = SettingsConfig.getAutoAnnouncerDuration();
        period = SettingsConfig.getAutoAnnouncerPeriod();
        if (taskId != 0)
            Bukkit.getScheduler().cancelTask(taskId);
        if (SettingsConfig.getAutoAnnouncerEnabled())
            launch();
    }

    private static void launch() {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(HoloBroadcast.getInstance(), () -> {
            HologramPlayersManager manager = HologramPlayersManager.getInstance();
            Bukkit.getOnlinePlayers().forEach(player -> {
                HologramPlayer hplayer = manager.getHologramPlayerFromUUID(player.getUniqueId());
                if (hplayer.getReceiveAutoAnnounces())
                    hplayer.showHUD(messages.get(cursor), duration);
            });
            cursor++;
            if (cursor >= messages.size())
                cursor = 0;
        }, period, period);
    }
}
