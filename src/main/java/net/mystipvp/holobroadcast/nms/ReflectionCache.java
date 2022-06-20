/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.nms;

import net.mystipvp.holobroadcast.HoloBroadcast;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public class ReflectionCache {

    /**
     * Classes
     */
    public static final Class<?> CraftWorld = ReflectionUtil.getOBCClass("CraftWorld"),
            CraftItemStack = ReflectionUtil.getOBCClass("inventory.CraftItemStack"),
            World = ReflectionUtil.getNMSClass("World", "world.level"),
            ItemStack = ReflectionUtil.getNMSClass("ItemStack", "world.item"),
            EntityArmorStand = ReflectionUtil.getNMSClass("EntityArmorStand", "world.entity.decoration"),
            PlayerConnection = ReflectionUtil.getNMSClass("PlayerConnection", "server.network"),
            PacketPlayOutEntityEquipment = ReflectionUtil.getNMSClass("PacketPlayOutEntityEquipment", "network.protocol.game"),
            PacketPlayOutSpawnEntityLiving = ReflectionUtil.getNMSClass("PacketPlayOutSpawnEntityLiving", "network.protocol.game", "PacketPlayOutSpawnEntity"),
            PacketPlayOutEntityDestroy = ReflectionUtil.getNMSClass("PacketPlayOutEntityDestroy", "network.protocol.game"),
            PacketPlayOutEntityMetadata = ReflectionUtil.getNMSClass("PacketPlayOutEntityMetadata", "network.protocol.game"),
            PacketPlayOutEntityTeleport = ReflectionUtil.getNMSClass("PacketPlayOutEntityTeleport", "network.protocol.game"),
            Entity = ReflectionUtil.getNMSClass("Entity", "world.entity"),
            EntityPlayer = ReflectionUtil.getNMSClass("EntityPlayer", "server.level"),
            DataWatcher = ReflectionUtil.getNMSClass("DataWatcher", "network.syncher"),
            EntityLiving = ReflectionUtil.getNMSClass("EntityLiving", "world.entity"),
            CraftPlayer = ReflectionUtil.getOBCClass("entity.CraftPlayer"),
            Packet = ReflectionUtil.getNMSClass("Packet", "network.protocol");

    public static Class<?> Pair = null;

    /**
     * Constructors
     */
    public static Constructor<?> EntityArmorStandConstructor = null,
            PacketPlayOutEntityEquipmentConstructor = null,
            PacketPlayOutSpawnEntityLivingConstructor = null,
            PacketPlayOutEntityDestroyConstructor = null,
            PacketPlayOutEntityMetadataConstructor = null,
            PacketPlayOutEntityTeleportConstructor = null,
            PairConstructor = null;

    /**
     * Methods
     */
    public static Method setInvisible, setCustomNameVisible, setMarker,
            setCustomName, getID, getDataWatcher,
            setLocation, setSmall, asNMSCopy, setFireTicks;

    public static boolean setNameUsesString = false, usesIntArray = false,
            is_119 = ReflectionUtil.version.contains("1_19"), usesPair = false, usesEnumItemSlot = false,
            supportsHex = ReflectionUtil.version.contains("1_16") || ReflectionUtil.version.contains("1_17")
                    || ReflectionUtil.version.contains("1_18") || is_119,
            is_117 = ReflectionUtil.version.contains("1_17") || ReflectionUtil.version.contains("1_18") || is_119,
            is_118 = ReflectionUtil.version.contains("1_18") || is_119;

    // Sets the Constructors/Methods values using reflection
    static {
        Optional<Class<?>> optionalPair = ReflectionUtil.optionalClass("com.mojang.datafixers.util.Pair");
        usesPair = optionalPair.isPresent() && (supportsHex);
        if (usesPair)
            Pair = optionalPair.get();
        try {
            EntityArmorStandConstructor = EntityArmorStand.getConstructor(World, double.class, double.class, double.class);
            PacketPlayOutSpawnEntityLivingConstructor = PacketPlayOutSpawnEntityLiving.getConstructor(EntityLiving);
            if (is_117) {
                try {
                    PacketPlayOutEntityDestroyConstructor = PacketPlayOutEntityDestroy.getConstructor(int.class);
                } catch (NoSuchMethodException e) {
                    usesIntArray = true;
                    PacketPlayOutEntityDestroyConstructor = PacketPlayOutEntityDestroy.getConstructor(int[].class);
                }
            } else
                PacketPlayOutEntityDestroyConstructor = PacketPlayOutEntityDestroy.getConstructor(int[].class);
            PacketPlayOutEntityMetadataConstructor = PacketPlayOutEntityMetadata.getConstructor(int.class, DataWatcher, boolean.class);
            PacketPlayOutEntityTeleportConstructor = PacketPlayOutEntityTeleport.getConstructor(Entity);
            try {
                setInvisible = EntityArmorStand.getMethod("setInvisible", boolean.class);
            } catch (NoSuchMethodException e) {
                setInvisible = EntityArmorStand.getMethod("j", boolean.class);
            }
            try {
                setCustomNameVisible = EntityArmorStand.getMethod("setCustomNameVisible", boolean.class);
            } catch (NoSuchMethodException e) {
                setCustomNameVisible = EntityArmorStand.getMethod("n", boolean.class);
            }
            try {
                setMarker = EntityArmorStand.getMethod("setMarker", boolean.class);
            } catch (NoSuchMethodException e) {
                setMarker = null;
            }
            try {
                setLocation = Entity.getMethod("setLocation", double.class, double.class, double.class, float.class, float.class);
            } catch (NoSuchMethodException e) {
                setLocation = Entity.getMethod("a", double.class, double.class, double.class, float.class, float.class);
            }
            try {
                setSmall = EntityArmorStand.getMethod("setSmall", boolean.class);
            } catch (NoSuchMethodException e) {
                setSmall = EntityArmorStand.getMethod("a", boolean.class);
            }
            try {
                setFireTicks = EntityArmorStand.getMethod("setFireTicks", int.class);
            } catch (NoSuchMethodException e) {
                setFireTicks = null;
            }
            try {
                getID = EntityArmorStand.getMethod("getId");
            } catch (NoSuchMethodException e) {
                getID = EntityArmorStand.getMethod("ae");
            }
            try {
                getDataWatcher = Entity.getMethod("getDataWatcher");
            } catch (NoSuchMethodException e) {
                getDataWatcher = Entity.getMethod("ai");
            }
            asNMSCopy = CraftItemStack.getMethod("asNMSCopy", org.bukkit.inventory.ItemStack.class);
            try {
                setCustomName = EntityArmorStand.getMethod("setCustomName", String.class);
                setNameUsesString = true;
            } catch (NoSuchMethodException x) {
                if (is_119) {
                    setCustomName = EntityArmorStand.getMethod("b", IChatBaseComponent.IChatBaseComponent);
                } else {
                    try {
                        setCustomName = EntityArmorStand.getMethod("setCustomName", IChatBaseComponent.IChatBaseComponent);
                    } catch (NoSuchMethodException e) {
                        setCustomName = EntityArmorStand.getMethod("a", IChatBaseComponent.IChatBaseComponent);
                    }
                }
            }
            if (ReflectionUtil.exists("EnumItemSlot", "world.entity")) {
                usesEnumItemSlot = true;
                try {
                    if (usesPair)
                        PacketPlayOutEntityEquipmentConstructor = PacketPlayOutEntityEquipment.getConstructor(int.class, List.class);
                    else
                        PacketPlayOutEntityEquipmentConstructor = PacketPlayOutEntityEquipment.getConstructor(int.class, ReflectionUtil.getNMSClass("EnumItemSlot", "world.entity"), ItemStack);
                } catch (NoSuchMethodException x) {
                    usesEnumItemSlot = false;
                    PacketPlayOutEntityEquipmentConstructor = PacketPlayOutEntityEquipment.getConstructor(int.class, int.class, ItemStack);
                }
            } else {
                PacketPlayOutEntityEquipmentConstructor = PacketPlayOutEntityEquipment.getConstructor(int.class, int.class, ItemStack);
            }
            if (usesPair)
                PairConstructor = Pair.getConstructor(Object.class, Object.class);
        } catch (NoSuchMethodException e) {
            HoloBroadcast.getInstance().getLogger().severe("Error while getting method/constructor:" + e.getMessage());
            e.printStackTrace();
        }
    }
}
