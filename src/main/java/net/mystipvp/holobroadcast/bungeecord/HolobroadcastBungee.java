/*
 * Copyright (c) 2020-2023.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.bungeecord;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.logging.Logger;

public class HolobroadcastBungee extends Plugin {

    public static String VERSION;

    @Override
    public void onEnable() {
        getProxy().registerChannel("BungeeCord");
        VERSION = getDescription().getVersion();

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new HolobroadcastCommand());

        Logger logger = getLogger();
        logger.info("O      O    OOAD");
        logger.info("L      L    L    C");
        logger.info("O      O    O    A");
        logger.info("HBROADCH    HAST");
        logger.info("O      O    O    S");
        logger.info("L      L    L    T");
        logger.info("O      O    OBRD");
    }
}
