/*
 * Copyright (c) 2020-2023.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.holograms.holograms;

import net.mystipvp.holobroadcast.animations.Animator;
import net.mystipvp.holobroadcast.config.files.AnimationsConfig;
import net.mystipvp.holobroadcast.holograms.Hologram;
import net.mystipvp.holobroadcast.nms.IChatBaseComponent;
import net.mystipvp.holobroadcast.nms.ReflectionCache;
import net.mystipvp.holobroadcast.nms.ReflectionUtil;
import net.mystipvp.holobroadcast.utils.MessageUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TextHologram extends Hologram {

    private final String rawLine;
    private final Object armorStand;
    private final int id;
    private final Object packetPlayOutSpawnEntityLiving;
    private final Object packetPlayOutEntityDestroy;
    private final boolean marker = ReflectionCache.setMarker != null;
    private final Map<String, Animator> animations = new HashMap<>();

    public TextHologram(Location location, Player viewer, String rawData) {
        super(location, viewer, 0.23D);

        this.rawLine = rawData;

        for (String s : AnimationsConfig.getAnimationsList()) {
            if (rawData.contains("%animation_" + s + "%"))
                animations.put(s, AnimationsConfig.getAnimation(s));
        }

        try {
            this.armorStand = ReflectionCache.EntityArmorStandConstructor.newInstance(ReflectionUtil.getHandle(Objects.requireNonNull(ReflectionCache.CraftWorld.cast(this.location.getWorld()))), this.location.getX(), this.location.getY(), this.location.getZ());

            ReflectionCache.setCustomNameVisible.invoke(armorStand, true);

            ReflectionCache.setInvisible.invoke(armorStand, true);
            if (ReflectionCache.setFireTicks != null)
                ReflectionCache.setFireTicks.invoke(armorStand, 10000);
            if (this.marker)
                ReflectionCache.setMarker.invoke(armorStand, true);

            this.packetPlayOutSpawnEntityLiving = ReflectionCache.PacketPlayOutSpawnEntityLivingConstructor.newInstance(armorStand);

            this.id = (int) ReflectionCache.getID.invoke(armorStand);
            if (ReflectionCache.is_117 && !ReflectionCache.usesIntArray)
                this.packetPlayOutEntityDestroy = ReflectionCache.PacketPlayOutEntityDestroyConstructor.newInstance(id);
            else
                this.packetPlayOutEntityDestroy = ReflectionCache.PacketPlayOutEntityDestroyConstructor.newInstance(new int[]{id});

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException |
                 InstantiationException e) {
            throw new RuntimeException("An error occurred while creating the hologram.", e);
        }
    }

    /**
     * @return the raw text
     */
    public String getRawLine() {
        return rawLine;
    }

    /**
     * Sets the EntityArmorStand custom name
     *
     * @param text Text to be displayed
     */
    private void setText(String text) {
        try {
            if (ReflectionCache.setNameUsesString) {
                ReflectionCache.setCustomName.invoke(armorStand, text);
            } else {
                ReflectionCache.setCustomName.invoke(armorStand, IChatBaseComponent.of(text));
            }
            updateMetadata();
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("An error occurred while changing hologram text.", e);
        }
    }

    /**
     * Update the EntityArmorStand Metadata only for one player
     *
     * @throws IllegalAccessException    Error
     * @throws InvocationTargetException Error
     * @throws InstantiationException    Error
     */
    private void updateMetadata() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Object packet;
        if (!ReflectionCache.is_1194)
            packet = ReflectionCache.PacketPlayOutEntityMetadataConstructor.newInstance(id, ReflectionCache.getDataWatcher.invoke(armorStand), true);
        else
            packet = ReflectionCache.PacketPlayOutEntityMetadataConstructor.newInstance(id, ReflectionCache.getNonDefaults.invoke(ReflectionCache.getDataWatcher.invoke(armorStand)));
        ReflectionUtil.sendPacket(viewer, packet);
    }

    /**
     * Updates the EntityArmorStand location
     */
    private void updateArmorStandLocation() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        ReflectionCache.setLocation.invoke(armorStand, this.location.getX(), marker ? this.location.getY() + 1.25D : this.location.getY() - 0.75, this.location.getZ(), this.location.getYaw(), this.location.getPitch());
        ReflectionUtil.sendPacket(viewer, ReflectionCache.PacketPlayOutEntityTeleportConstructor.newInstance(armorStand));
    }


    /**
     * Creates the Hologram.
     */
    @Override
    public void create() {
        try {
            ReflectionUtil.sendPacket(viewer, packetPlayOutSpawnEntityLiving);
            updateMetadata();
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException("An error occurred while spawning the hologram.", e);
        }
    }

    /**
     * Removes the Hologram.
     */
    @Override
    public void destroy() {
        ReflectionUtil.sendPacket(viewer, packetPlayOutEntityDestroy);
    }

    /**
     * Updates the Hologram. Called by the HologramPlayer's async task every tick.
     */
    @Override
    public void update() {
        String replacedAnimation = rawLine;
        for (String s : animations.keySet())
            replacedAnimation = replacedAnimation.replaceAll("%animation_" + s + "%", animations.get(s).nextFrame());
        if (replacedAnimation == null || replacedAnimation.length() < 1)
            replacedAnimation = " ";
        setText(MessageUtil.format(viewer, replacedAnimation));
    }

    /**
     * Moves the Hologram to a new location.
     *
     * @param newLocation the new Location
     */
    @Override
    public void move(Location newLocation) {
        try {
            this.updateArmorStandLocation();
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("An error occurred while moving the hologram.", e);
        }
    }
}
