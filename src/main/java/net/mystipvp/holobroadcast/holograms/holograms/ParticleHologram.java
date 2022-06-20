/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.holograms.holograms;

import net.mystipvp.holobroadcast.holograms.Hologram;
import net.mystipvp.holobroadcast.holograms.HologramPlayer;
import net.mystipvp.holobroadcast.holograms.HologramPlayersManager;
import net.mystipvp.holobroadcast.particles.ParticleType;
import net.mystipvp.holobroadcast.particles.ParticlesUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ParticleHologram extends Hologram {

    private final ParticleType type;
    private final int amount;
    private final int refreshRate;
    private int count = 0;

    public ParticleHologram(Location location, Player viewer, ParticleType type, int amount, int refreshRate) {
        super(location, viewer, 0.0D);
        this.type = type;
        this.amount = amount;
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
            if (hologramPlayer.getReceiveParticles()) {
                ParticlesUtil.spawnParticle(this.viewer, type, this.location.clone().add(0, 1.5, 0), amount);
            }
        } else
            count++;
    }

    @Override
    public void move(Location newLocation) {
    }
}
