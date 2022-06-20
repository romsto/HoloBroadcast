/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.holograms;

import net.mystipvp.holobroadcast.holograms.holograms.EmptyHologram;
import net.mystipvp.holobroadcast.holograms.holograms.ItemHologram;
import net.mystipvp.holobroadcast.holograms.holograms.SoundHologram;
import net.mystipvp.holobroadcast.holograms.holograms.TextHologram;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HologramFactory {

    private static HologramFactory instance;

    private HologramFactory() {

    }

    /**
     * @return the instance of the HologramFactory
     */
    public static HologramFactory getInstance() {
        if (instance == null) {
            instance = new HologramFactory();
        }
        return instance;
    }

    /**
     * Builds and returns a new instance of a TextHologram.
     *
     * @param location the location where to spawn the TextHologram
     * @param viewer   the player who will see the TextHologram
     * @param rawData  the message to display
     * @return the instance of the created TextHologram
     */
    public TextHologram buildTextHologram(Location location, Player viewer, String rawData) {
        return new TextHologram(location, viewer, rawData);
    }

    /**
     * Builds and returns a new instance of a EmptyHologram.
     *
     * @param location the location where to spawn the EmptyHologram
     * @param viewer   the player who will see the EmptyHologram
     * @return the instance of the created EmptyHologram
     */
    public EmptyHologram buildEmptyHologram(Location location, Player viewer) {
        return new EmptyHologram(location, viewer);
    }

    /**
     * Builds and returns a new instance of a ItemHologram.
     *
     * @param location the location where to spawn the ItemHologram
     * @param viewer   the player who will see the ItemHologram
     * @param item     the item that will be displayed
     * @return the instance of the created ItemHologram
     */
    public ItemHologram buildItemHologram(Location location, Player viewer, ItemStack item) {
        return new ItemHologram(location, viewer, item);
    }

    /**
     * Builds and returns a new instance of a SoundHologram.
     *
     * @param location    the location where to spawn the SoundHologram
     * @param viewer      the player who will see the SoundHologram
     * @param sound       the sound that will be played
     * @param refreshRate the numbers of ticks between two sounds
     * @return the instance of the created SoundHologram
     */
    public SoundHologram buildSoundHologram(Location location, Player viewer, String sound, int refreshRate) {
        return new SoundHologram(location, viewer, sound, refreshRate);
    }
}
