/*
 * Copyright (c) 2020-2023.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.listeners;

import net.mystipvp.holobroadcast.config.files.SettingsConfig;
import net.mystipvp.holobroadcast.holograms.HologramPlayer;
import net.mystipvp.holobroadcast.holograms.HologramPlayersManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class ConnectionListeners implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        HologramPlayersManager manager = HologramPlayersManager.getInstance();
        HologramPlayer hologramPlayer = manager.createHologramPlayer(uuid);

        if (!event.getPlayer().hasPlayedBefore()) {
            if (SettingsConfig.getWelcomeMessageEnabled())
                hologramPlayer.showDelayedHUD(SettingsConfig.getWelcomeMessageText(), SettingsConfig.getWelcomeMessageDuration(), SettingsConfig.getWelcomeSendDelay());
        } else {
            if (SettingsConfig.getJoinMessageEnabled())
                hologramPlayer.showDelayedHUD(SettingsConfig.getJoinMessageText(), SettingsConfig.getJoinMessageDuration(), SettingsConfig.getJoinSendDelay());
        }
    }


    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        HologramPlayersManager manager = HologramPlayersManager.getInstance();
        manager.deleteHologramPlayer(uuid);
    }
}
