/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.hooks;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.events.PlayerEnterPlotEvent;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.flag.PlotFlag;
import com.plotsquared.core.plot.flag.implementations.ServerPlotFlag;
import com.sk89q.worldedit.util.eventbus.Subscribe;
import net.mystipvp.holobroadcast.HoloBroadcast;
import net.mystipvp.holobroadcast.config.files.SettingsConfig;
import net.mystipvp.holobroadcast.holograms.HologramPlayersManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.logging.Logger;

public class PlotSquaredHook {

    public PlotSquaredHook() {
        Logger logger = HoloBroadcast.getInstance().getLogger();
        if (Bukkit.getPluginManager().isPluginEnabled("PlotSquared")) {
            if (Bukkit.getPluginManager().getPlugin("PlotSquared").getDescription().getVersion().startsWith("6")) {
                PlotAPI plotAPI = new PlotAPI();
                plotAPI.registerListener(this);
                logger.info("Using PlotSquared v6");
            } else {
                logger.warning("PlotSquared is detected, however its version is outdated and isn't supported by Holobroadcast :(");
            }
        }
    }

    @Subscribe
    public void onPlayerEnterPlot(PlayerEnterPlotEvent e) {
        if (SettingsConfig.getPlotEnterEnabled()) {
            Plot plot = e.getPlot();
            if (plot == null || plot.getOwner() == null)
                return;
            OfflinePlayer player = Bukkit.getOfflinePlayer(plot.getOwner());
            String name = player.getName();
            if (name == null)
                return;

            boolean cancel = false;
            if (SettingsConfig.isServerPlotDisable())
                for (PlotFlag<?, ?> flag : plot.getFlags())
                    if (flag instanceof ServerPlotFlag && ((ServerPlotFlag) flag).getValue()) {
                        cancel = true;
                        break;
                    }
            for (String s : SettingsConfig.getPlotOwnersDisableList()) {
                if (name.equalsIgnoreCase(s)) {
                    cancel = true;
                    break;
                }
            }
            if (!cancel)
                HologramPlayersManager.getInstance().getHologramPlayerFromUUID(e.getPlotPlayer().getUUID()).showHUD(SettingsConfig.getPlotEnterMessageText().replaceAll("%ownername%", name), SettingsConfig.getPlotEnterMessageDuration());
        }
    }
}