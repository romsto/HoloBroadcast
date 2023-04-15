/*
 * Copyright (c) 2020-2023.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.holograms;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import net.mystipvp.holobroadcast.holograms.holograms.*;
import net.mystipvp.holobroadcast.particles.ParticleType;
import net.mystipvp.holobroadcast.utils.Patterns;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permissible;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

public abstract class HologramHolder {

    protected final String name;
    protected final Permissible creator;
    protected final Player viewer;
    protected final double heightAverage;
    private final double height;
    /**
     * The rawLines list is a String version of the holograms list
     */

    protected List<String> rawLines;
    protected List<Hologram> holograms;
    protected Location location;

    /**
     * Instantiates a new HologramHolder. If the creator is set to null, permissions won't have
     * any effect.
     *
     * @param name     the identifier of the current HologramHolder
     * @param creator  the Permissible whose permissions will be checked in order to apply placeholders
     * @param location the Location where the current HologramHolder will be displayed
     * @param viewer   the player who will see the current HologramHolder
     * @param rawData  the raw String data
     */
    public HologramHolder(String name, @Nullable CommandSender creator, @Nonnull Location location, @Nonnull Player viewer, @Nonnull String rawData) {
        this.rawLines = new ArrayList<>();
        this.holograms = new ArrayList<>();

        this.name = name;
        this.creator = creator == null ? Bukkit.getServer().getConsoleSender() : creator;
        this.location = location;
        this.viewer = viewer;

        double totalHeight = 0;
        Location spawnLocation = this.location.clone().subtract(0, totalHeight, 0);

        assert creator != null;

        rawData = rawData.replaceAll("%command_sender_name%", creator.getName());

        Matcher placeholderMatcher = Patterns.PLACEHOLDERS_PATTERN.matcher(rawData);

        int lastOccurence = 0;

        while (placeholderMatcher.find()) {
            int index = placeholderMatcher.start();

            if (index > 0 && index - lastOccurence > 0) {
                String text = rawData.substring(lastOccurence, index);
                this.rawLines.add(text);
                this.holograms.add(new TextHologram(spawnLocation.subtract(0, 0.23 / 2, 0), this.viewer, text));
            }

            lastOccurence = placeholderMatcher.end();

            if (placeholderMatcher.group(1) != null && this.creator.hasPermission("holobroadcast.blankline")) {
                this.rawLines.add(" ");
                this.holograms.add(new EmptyHologram(spawnLocation.subtract(0, 0.23 / 2, 0), this.viewer));
            }

            if (placeholderMatcher.group(4) != null && this.creator.hasPermission("holobroadcast.item")) {
                String materialString = placeholderMatcher.group(4);
                Optional<XMaterial> xMaterial = XMaterial.matchXMaterial(materialString);
                if (xMaterial.isPresent()) {
                    ItemStack item = xMaterial.get().parseItem();
                    assert item != null;
                    this.rawLines.add(materialString);
                    this.holograms.add(new ItemHologram(spawnLocation.subtract(0, 0.55 / 2, 0), this.viewer, item));
                }
            }

            if (placeholderMatcher.group(6) != null && placeholderMatcher.group(7) != null && this.creator.hasPermission("holobroadcast.sound")) {
                String soundString = placeholderMatcher.group(7);
                int refresh = Integer.parseInt(placeholderMatcher.group(6));

                if (XSound.matchXSound(soundString).isPresent()) {
                    this.rawLines.add(soundString);
                    this.holograms.add(new SoundHologram(spawnLocation, this.viewer, soundString, refresh));
                }
            }

            if (placeholderMatcher.group(10) != null && placeholderMatcher.group(11) != null && this.creator.hasPermission("holobroadcast.particle")) {
                String particleString = placeholderMatcher.group(11);
                int refresh = Integer.parseInt(placeholderMatcher.group(10));
                int amount = 1;
                if (placeholderMatcher.group(13) != null)
                    amount = Integer.parseInt(placeholderMatcher.group(13));
                Optional<ParticleType> particleType = ParticleType.getParticle(particleString);
                if (particleType.isPresent()) {
                    this.rawLines.add(particleString);
                    this.holograms.add(new ParticleHologram(spawnLocation, this.viewer, particleType.get(), amount, refresh));
                }
            }
        }

        if (lastOccurence < rawData.length()) {
            String text = rawData.substring(lastOccurence);
            this.rawLines.add(text);
            this.holograms.add(new TextHologram(spawnLocation.subtract(0, 0.23 / 2, 0), this.viewer, text));
        }

        for (Hologram hologram : holograms)
            totalHeight += hologram.getHeight();

        height = totalHeight;
        heightAverage = height / holograms.size();
    }


    /*
    GETTERS
     */

    /**
     * @return the name of the HologramHolder
     */
    public String getName() {
        return name;
    }

    /**
     * @return the current location of the HologramHolder
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the current location (updates all the holograms' location)
     *
     * @param location new location
     */
    public void setLocation(Location location) {
        this.location = location.add(0, heightAverage / 2, 0);

        double currentheight = 0;
        for (Hologram hologram : holograms) {
            hologram.setLocation(location.clone().subtract(0, currentheight + hologram.getHeight() / 2, 0));
            currentheight += hologram.getHeight();
        }
    }

    /**
     * @return returns all the holograms
     */
    public List<Hologram> getHolograms() {
        return holograms;
    }

    /**
     * @return the viewer
     */
    public Player getViewer() {
        return viewer;
    }

    /*
    SETTERS
     */

    /**
     * @return the total height of the HologramHolder
     */
    public double getHeight() {
        return height;
    }

    /*
    METHODS
     */

    /**
     * Creates all the holograms
     */
    public void create() {
        holograms.forEach(Hologram::create);
    }

    /**
     * Removes all the holograms
     */
    public void remove() {
        holograms.forEach(Hologram::destroy);
    }

    /**
     * Updates all the current HologramHolder's holograms.
     */
    public void update() {
        this.onUpdate();
        holograms.forEach(Hologram::update);
    }

    /**
     * Called by the {@link #update()} method.
     */
    public abstract void onUpdate();
}
