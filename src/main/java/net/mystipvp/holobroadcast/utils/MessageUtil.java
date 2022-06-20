/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.utils;

import net.mystipvp.holobroadcast.HoloBroadcast;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

public class MessageUtil {

    /**
     * Colorize the string (& -> §)
     *
     * @param string String
     * @return Colored String
     */
    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Colorize and change Placeholders for a specific player.
     * This method uses either PlaceholderAPI or MVdWPlaceHolderAPI.
     *
     * @param player Player
     * @param string String
     * @return Colored String
     */
    public static String format(OfflinePlayer player, String string) {
        String replaced = HoloBroadcast.getPlaceholdersHook().replace(player, string);
        return MessageUtil.color(replaced);
    }
}
