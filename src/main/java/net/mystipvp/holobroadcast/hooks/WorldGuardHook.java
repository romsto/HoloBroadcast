/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.hooks;

import net.mystipvp.holobroadcast.HoloBroadcast;
import net.mystipvp.holobroadcast.config.files.SettingsConfig;
import org.bukkit.Bukkit;

import java.util.logging.Logger;

public class WorldGuardHook {

    public static boolean newVersion = true;
    private static Logger logger;

    public static void init() {
        logger = HoloBroadcast.getInstance().getLogger();
        try {
            Class.forName("com.sk89q.worldguard.WorldGuard");
        } catch (ClassNotFoundException e) {
            newVersion = false;
        }
        if (newVersion) {
            GreetingFlagHandler.registerFlag();
            FarewellFlagHandler.registerFlag();
        }
    }

    public static void load() {
        if (!SettingsConfig.getWorldGuardActive())
            return;

        if (Bukkit.getPluginManager().isPluginEnabled("WorldGuard") && newVersion) {
            logger.info("Using WorldGuard v7");
            GreetingFlagHandler.registerFactory();
            FarewellFlagHandler.registerFactory();
        } else if (Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
            logger.info("WorldGuard bellow version 7 isn't compatible with HoloBroadcast.");
        }
    }
}