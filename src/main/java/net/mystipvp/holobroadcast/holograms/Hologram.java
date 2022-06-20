/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.holograms;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class Hologram {

    protected final Player viewer;
    protected final double height;
    protected Location location;

    public Hologram(Location location, Player viewer, double height) {
        this.location = location;
        this.viewer = viewer;
        this.height = height;
    }

    /*
    GETTERS
     */

    /**
     * @return the current location of the hologram
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Changes the location of the Hologram.
     *
     * @param location new Location
     */
    public void setLocation(Location location) {
        this.location = location;
        this.move(location);
    }

    /**
     * @return the height of the hologram
     */
    public double getHeight() {
        return height;
    }

    /*
    SETTERS
     */

    /**
     * @return the player viewing the hologram
     */
    public Player getViewer() {
        return viewer;
    }

    /*
    METHODS
     */

    /**
     * Creates the Hologram.
     */
    public abstract void create();

    /**
     * Removes the Hologram.
     */
    public abstract void destroy();

    /**
     * Called when the Hologram is updated (every tick).
     */
    public abstract void update();

    /**
     * Moves the Hologram to a new location.
     *
     * @param newLocation the new Location
     */
    public abstract void move(Location newLocation);
}
