/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.hooks;

import me.clip.placeholderapi.PlaceholderAPI;
import net.mystipvp.holobroadcast.HoloBroadcast;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.logging.Logger;

public class PlaceholdersHook {

    private boolean placeholderapi = false;

    public PlaceholdersHook() {
        Logger logger = HoloBroadcast.getInstance().getLogger();
        placeholderapi = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
        if (placeholderapi)
            logger.info("Using PlaceholderAPI");
    }

    /**
     * Returns whether PlaceholderAPI is present or not in the server
     *
     * @return
     */
    public boolean isEnabled() {
        return placeholderapi;
    }

    /**
     * Replace all the placeholders in a string
     *
     * @param player Player To Send
     * @param string Message
     * @return Replaced placeholders
     */
    public String replace(OfflinePlayer player, String string) {
        if (placeholderapi) {
            return PlaceholderAPI.setPlaceholders(player, string);
        }

        return string;
    }
}
