/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.holograms.holograms;

import net.mystipvp.holobroadcast.HoloBroadcast;
import net.mystipvp.holobroadcast.holograms.Hologram;
import net.mystipvp.holobroadcast.nms.ReflectionCache;
import net.mystipvp.holobroadcast.nms.ReflectionUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemHologram extends Hologram {

    private final Object itemStack;
    private final Object armorStand;
    private final int id;
    private final Object packetPlayOutSpawnEntityLiving;
    private final Object packetPlayOutEntityDestroy;
    private final boolean isBlock;
    private float rotation = new Random().nextFloat() * 100f;

    public ItemHologram(Location location, Player viewer, ItemStack item) {
        super(location, viewer, 0.55);
        isBlock = item.getType().isBlock();

        try {
            this.armorStand = ReflectionCache.EntityArmorStandConstructor.newInstance(ReflectionUtil.getHandle(ReflectionCache.CraftWorld.cast(this.location.getWorld())), this.location.getX(), this.location.getY(), this.location.getZ());
            ReflectionCache.setInvisible.invoke(armorStand, true);
            ReflectionCache.setSmall.invoke(armorStand, true);
            boolean marker = ReflectionCache.setMarker != null;
            if (marker)
                ReflectionCache.setMarker.invoke(armorStand, true);
            boolean firetick = ReflectionCache.setFireTicks != null;
            if (firetick)
                ReflectionCache.setFireTicks.invoke(armorStand, 10000);
            this.id = (int) ReflectionCache.getID.invoke(armorStand);
            this.itemStack = ReflectionCache.asNMSCopy.invoke(null, item);
            this.packetPlayOutSpawnEntityLiving = ReflectionCache.PacketPlayOutSpawnEntityLivingConstructor.newInstance(armorStand);
            if (ReflectionCache.is_117 && !ReflectionCache.usesIntArray)
                this.packetPlayOutEntityDestroy = ReflectionCache.PacketPlayOutEntityDestroyConstructor.newInstance(id);
            else
                this.packetPlayOutEntityDestroy = ReflectionCache.PacketPlayOutEntityDestroyConstructor.newInstance(new int[]{id});
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException |
                 InstantiationException e) {
            throw new RuntimeException("An error occurred while creating the item hologram", e);
        }
    }

    /**
     * Sets the EntityArmorStand item
     *
     * @param itemStack ItemStack NMS
     */
    private void sendItem(Object itemStack) {
        try {
            if (!ReflectionCache.usesEnumItemSlot && !ReflectionCache.usesPair) {
                ReflectionUtil.sendPacket(viewer, ReflectionCache.PacketPlayOutEntityEquipmentConstructor.newInstance(id, 4, itemStack));
            } else if (ReflectionCache.usesPair) {
                List<?> l = new ArrayList<>();
                Method add = List.class.getDeclaredMethod("add", Object.class);
                add.invoke(l, ReflectionCache.PairConstructor.newInstance(ReflectionUtil.getNMSClass("EnumItemSlot", "world.entity").getEnumConstants()[5], itemStack));
                ReflectionUtil.sendPacket(viewer, ReflectionCache.PacketPlayOutEntityEquipmentConstructor.newInstance(id, l));
            } else {
                ReflectionUtil.sendPacket(viewer, ReflectionCache.PacketPlayOutEntityEquipmentConstructor.newInstance(id, ReflectionUtil.getNMSClass("EnumItemSlot", "world.entity").getEnumConstants()[5], itemStack));
            }
            updateMetadata();
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException |
                 NoSuchMethodException e) {
            HoloBroadcast.getInstance().getLogger().severe("An error occurred while setting hologram item: " + e.getMessage());
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
        Object packet = ReflectionCache.PacketPlayOutEntityMetadataConstructor.newInstance(id, ReflectionCache.getDataWatcher.invoke(armorStand), true);
        ReflectionUtil.sendPacket(viewer, packet);
    }

    /**
     * Updates the EntityArmorStand location
     */
    private void updateArmorStandLocation() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        ReflectionCache.setLocation.invoke(armorStand, this.location.getX(), isBlock ? this.location.getY() + 0.75D : this.location.getY() + 0.34D, this.location.getZ(), rotation, this.location.getPitch());
        ReflectionUtil.sendPacket(viewer, ReflectionCache.PacketPlayOutEntityTeleportConstructor.newInstance(armorStand));
    }

    /**
     * Creates the Hologram.
     */
    @Override
    public void create() {
        ReflectionUtil.sendPacket(viewer, packetPlayOutSpawnEntityLiving);
        sendItem(itemStack);
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
        // Changes the item rotation
        rotation += 1.8;
        if (rotation >= 180)
            rotation = -180f;
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
            HoloBroadcast.getInstance().getLogger().severe("An error occurred while moving an item hologram: " + e.getMessage());
        }
    }
}
