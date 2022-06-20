/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.holograms.hologramholders;

import net.mystipvp.holobroadcast.config.files.SettingsConfig;
import net.mystipvp.holobroadcast.holograms.HologramHolder;
import net.mystipvp.holobroadcast.holograms.HologramPlayer;
import net.mystipvp.holobroadcast.holograms.HologramPlayersManager;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class HologramHUD extends HologramHolder {

    public final long lifetime;
    public final String rawData;
    public final CommandSender creator;
    private final boolean lockHUDY;
    private final Location relative;
    private final int distance;
    public long duration = 0;

    public HologramHUD(CommandSender creator, Location location, Player viewer, String rawData, long lifeTime) {
        super("hud-" + viewer.getUniqueId(), creator, location, viewer, rawData);
        this.lifetime = lifeTime;
        this.lockHUDY = SettingsConfig.getLockHUDY();
        this.relative = SettingsConfig.getRelativeLocationDisplay(viewer.getWorld());
        this.rawData = rawData;
        this.creator = creator;

        HologramPlayersManager manager = HologramPlayersManager.getInstance();
        HologramPlayer hologramPlayer = manager.getHologramPlayerFromUUID(viewer.getUniqueId());
        this.distance = hologramPlayer.getHUDDistance();
    }

    /**
     * @return the lifetime of the HUD
     */
    public long getLifetime() {
        return lifetime;
    }

    /**
     * Called every ticks to update the holder
     */
    @Override
    public void onUpdate() {
        duration++;

        if (duration > lifetime) {
            HologramPlayersManager manager = HologramPlayersManager.getInstance();
            HologramPlayer hologramPlayer = manager.getHologramPlayerFromUUID(viewer.getUniqueId());
            hologramPlayer.deleteHUD();
            return;
        }

        Vector playerDirection = viewer.getEyeLocation().getDirection().normalize();
        Location newLocation = viewer.getLocation().add(playerDirection.multiply(distance)).add(relative);
        if (lockHUDY)
            newLocation.setY(viewer.getLocation().getY() + relative.getY());
        newLocation.add(0, (getHeight() - heightAverage) / 2, 0);
        this.setLocation(newLocation);
    }
}
