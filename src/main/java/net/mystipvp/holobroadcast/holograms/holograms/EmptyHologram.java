/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.holograms.holograms;

import net.mystipvp.holobroadcast.holograms.Hologram;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class EmptyHologram extends Hologram {

    public EmptyHologram(Location location, Player viewer) {
        super(location, viewer, 0.23D);
    }

    @Override
    public void create() {
        // Does nothing, it is an empty hologram.
    }

    @Override
    public void destroy() {
        // Does nothing, it is an empty hologram.
    }

    @Override
    public void update() {
        // Does nothing, it is an empty hologram.
    }

    @Override
    public void move(Location newLocation) {
        // Does nothing, it is an empty hologram.
    }
}
