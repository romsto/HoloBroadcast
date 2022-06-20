/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.nms;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ReflectionUtil {

    private static final Map<String, Class<?>> cached_classes = new HashMap<>();
    public static String version;
    private static MethodHandle PLAYER_GET_HANDLE, PLAYER_CONNECTION, SEND_PACKET;

    // Get NMS version of the server
    // We use this in the packet paths
    static {
        String[] versionArray = Bukkit.getServer().getClass().getName().replace('.', ',').split(",");
        if (versionArray.length >= 4) {
            version = versionArray[3];
        } else {
            version = "";
        }
    }

    /**
     * Get the net.minecraft.server class
     *
     * @param className Name of the class
     * @return NMS class
     */
    public static Class<?> getNMSClass(String className) {
        if (cached_classes.containsKey(className))
            return cached_classes.get(className);
        try {
            Class<?> c = Class.forName("net.minecraft.server." + version + "." + className);
            cached_classes.put(className, c);
            return c;
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("An error occurred while finding NMS class.", ex);
        }
    }

    /**
     * Get the net.minecraft.server class
     *
     * @param className      Name of the class
     * @param pathRefactored new path from the 1.17 version
     * @return NMS class
     */
    public static Class<?> getNMSClass(String className, String pathRefactored) {
        if (cached_classes.containsKey(className))
            return cached_classes.get(className);
        try {
            if (version.contains("1_17") || version.contains("1_18") || version.contains("1_19")) {
                Class<?> c = Class.forName("net.minecraft." + pathRefactored + (pathRefactored.equals("") ? "" : ".") + className);
                cached_classes.put(className, c);
                return c;
            }
            Class<?> c = Class.forName("net.minecraft.server." + version + "." + className);
            cached_classes.put(className, c);
            return c;
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("An error occurred while finding NMS class.", ex);
        }
    }

    /**
     * Get the net.minecraft.server class
     *
     * @param className      Name of the class
     * @param pathRefactored new path from the 1.17 version
     * @param className119   Name of the class since 1.19
     * @return NMS class
     */
    public static Class<?> getNMSClass(String className, String pathRefactored, String className119) {
        if (cached_classes.containsKey(className))
            return cached_classes.get(className);
        try {
            if (version.contains("1_17") || version.contains("1_18") || version.contains("1_19")) {
                Class<?> c = Class.forName("net.minecraft." + pathRefactored + (pathRefactored.equals("") ? "" : ".") + (!version.contains("1_19") ? className : className119));
                cached_classes.put(className, c);
                return c;
            }
            Class<?> c = Class.forName("net.minecraft.server." + version + "." + className);
            cached_classes.put(className, c);
            return c;
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("An error occurred while finding NMS class.", ex);
        }
    }

    /**
     * Check if the class exists
     *
     * @param className NMS class
     * @return boolean exists
     */
    public static boolean exists(String className) {
        try {
            Class<?> c = Class.forName("net.minecraft.server." + version + "." + className);
            cached_classes.put(className, c);
            return true;
        } catch (ClassNotFoundException ex) {
            return false;
        }
    }

    /**
     * Check if the class exists
     *
     * @param className NMS class
     * @return boolean exists
     */
    public static boolean exists(String className, String pathRefactor) {
        try {
            if (version.contains("1_17") || version.contains("1_18") || version.contains("1_19")) {
                Class<?> c = Class.forName("net.minecraft." + pathRefactor + (pathRefactor.equals("") ? "" : ".") + className);
                cached_classes.put(className, c);
                return true;
            }
            Class<?> c = Class.forName("net.minecraft.server." + version + "." + className);
            cached_classes.put(className, c);
            return true;
        } catch (ClassNotFoundException ex) {
            return false;
        }
    }

    /**
     * Get the org.bukkit.craftbukkit class
     *
     * @param className Name of the class
     * @return CraftBukkit class
     */
    public static Class<?> getOBCClass(String className) {
        if (cached_classes.containsKey(className))
            return cached_classes.get(className);
        try {
            Class<?> c = Class.forName("org.bukkit.craftbukkit." + version + "." + className);
            cached_classes.put(className, c);
            return c;
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("An error occurred while finding OBC class.", ex);
        }
    }

    /**
     * Get the method getHandle()
     *
     * @param object Class
     * @return getHandle() returning
     * @throws NoSuchMethodException     Exception
     * @throws InvocationTargetException Exception
     * @throws IllegalAccessException    Exception
     */
    public static Object getHandle(Object object) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return object.getClass().getMethod("getHandle").invoke(object);
    }

    /**
     * Get an enum value
     *
     * @param enumClass Enum
     * @param enumName  Value
     * @return Enum Value
     */
    public static Object enumValueOf(Class<?> enumClass, String enumName) {
        return Enum.valueOf((Class<Enum>) enumClass, enumName.toUpperCase());
    }

    /**
     * Get an Optional NMS Class
     *
     * @param className Name of the NMS Class
     * @return Optional Class
     */
    public static Optional<Class<?>> getNMSOptionalClass(String className) {
        return optionalClass("net.minecraft.server." + version + "." + className);
    }

    /**
     * Get an Optional OBC Class
     *
     * @param className Name of the NMS Class
     * @return Optional Class
     */
    public static Optional<Class<?>> getOBCOptionalClass(String className) {
        return optionalClass("org.bukkit.craftbukkit." + version + "." + className);
    }

    /**
     * Get a Class through an optional
     *
     * @param className Class name
     * @return Optional Class
     */
    public static Optional<Class<?>> optionalClass(String className) {
        try {
            return Optional.of(Class.forName(className));
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    /**
     * Sends a packet to the Player's connection.
     *
     * @param player Receiver
     * @param packet Packet
     */
    public static void sendPacket(Player player, Object packet) {

        if (PLAYER_GET_HANDLE == null) {
            try {
                MethodHandles.Lookup lookup = MethodHandles.lookup();
                Field playerConnectionField = Arrays.stream(ReflectionCache.EntityPlayer.getFields())
                        .filter(field -> field.getType().isAssignableFrom(ReflectionCache.PlayerConnection))
                        .findFirst().orElseThrow(NoSuchFieldException::new);

                PLAYER_GET_HANDLE = lookup.findVirtual(ReflectionCache.CraftPlayer, "getHandle", MethodType.methodType(ReflectionCache.EntityPlayer));
                PLAYER_CONNECTION = lookup.unreflectGetter(playerConnectionField);
                if (ReflectionCache.is_118)
                    SEND_PACKET = lookup.findVirtual(ReflectionCache.PlayerConnection, "a", MethodType.methodType(void.class, ReflectionCache.Packet));
                else
                    SEND_PACKET = lookup.findVirtual(ReflectionCache.PlayerConnection, "sendPacket", MethodType.methodType(void.class, ReflectionCache.Packet));
            } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        try {
            Object entityPlayer = PLAYER_GET_HANDLE.invoke(player);
            Object playerConnection = PLAYER_CONNECTION.invoke(entityPlayer);
            SEND_PACKET.invoke(playerConnection, packet);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
