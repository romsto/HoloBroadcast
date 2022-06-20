/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.listeners;

import net.mystipvp.holobroadcast.holograms.HologramPlayer;
import net.mystipvp.holobroadcast.holograms.HologramPlayersManager;
import net.mystipvp.holobroadcast.holograms.hologramholders.HologramHUD;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.util.UUID;

public class MovementsListeners implements Listener {

    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        HologramPlayersManager manager = HologramPlayersManager.getInstance();
        HologramPlayer hologramPlayer = manager.createHologramPlayer(uuid);

        if (hologramPlayer.hasHUD()) {
            HologramHUD hud = hologramPlayer.hologramHUD;
            hologramPlayer.deleteHUD();
            hologramPlayer.showHUD(hud.creator, hud.rawData, hud.lifetime - hud.duration);
        }
    }

}
