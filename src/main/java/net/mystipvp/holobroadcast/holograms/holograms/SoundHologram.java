/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.holograms.holograms;

import com.cryptomorin.xseries.XSound;
import net.mystipvp.holobroadcast.holograms.Hologram;
import net.mystipvp.holobroadcast.holograms.HologramPlayer;
import net.mystipvp.holobroadcast.holograms.HologramPlayersManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SoundHologram extends Hologram {

    private final String sound;
    private final int refreshRate;
    private int count = 0;

    public SoundHologram(Location location, Player viewer, String sound, int refreshRate) {
        super(location, viewer, 0.0D);
        this.sound = sound;
        this.refreshRate = refreshRate;
    }

    @Override
    public void create() {
        // Does nothing
    }

    @Override
    public void destroy() {
        // Doest nothing
    }

    @Override
    public void update() {

        assert this.viewer != null;
        HologramPlayersManager manager = HologramPlayersManager.getInstance();
        HologramPlayer hologramPlayer = manager.getHologramPlayerFromUUID(this.viewer.getUniqueId());


        if (count >= refreshRate) {
            count = 0;
            if (hologramPlayer.receiveHologramsSounds()) {
                XSound.play(viewer, sound);
            }
        } else
            count++;
    }

    @Override
    public void move(Location newLocation) {
    }
}
