/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.config.files;

import net.mystipvp.holobroadcast.HoloBroadcast;
import net.mystipvp.holobroadcast.config.CustomConfig;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

public class PlayersDataConfig {

    private static final CustomConfig playersDataConfig = HoloBroadcast.getPlayersDataConfig();
    private static final FileConfiguration config = playersDataConfig.getConfig();

    private static final String RECEIVE_HUD_MESSAGE = "a";
    private static final String HUD_DISTANCE = "b";
    private static final String RECEIVE_HOLOGRAMS_SOUNDS = "c";
    private static final String RECEIVE_HUD_IN_COMBAT = "d";
    private static final String RECEIVE_AUTO_ANNOUNCES = "e";
    private static final String RECEIVE_PARTICLES = "f";


    /**
     * Gets the "receive HUD message" ("a" in the config) parameter of a Player.
     *
     * @param uuid the player's UUID
     * @return the value of this parameter
     */
    public static Boolean getReceiveHUDMessage(UUID uuid) {
        return config.getBoolean(uuid.toString() + "." + RECEIVE_HUD_MESSAGE);
    }

    public static void setReceiveHudMessage(UUID uuid, boolean value) {
        config.set(uuid.toString() + "." + RECEIVE_HUD_MESSAGE, value);
    }

    public static int getHUDDistance(UUID uuid) {
        return config.getInt(uuid.toString() + "." + HUD_DISTANCE);
    }

    public static void setHUDDistance(UUID uuid, int value) {
        config.set(uuid.toString() + "." + HUD_DISTANCE, value);
    }

    public static boolean getReceiveHologramsSounds(UUID uuid) {
        if (config.contains(uuid.toString() + "." + RECEIVE_HOLOGRAMS_SOUNDS)) {
            return config.getBoolean(uuid + "." + RECEIVE_HOLOGRAMS_SOUNDS);
        } else {
            setReceiveHologramsSounds(uuid, true);
            return true;
        }
    }

    public static void setReceiveHologramsSounds(UUID uuid, boolean value) {
        config.set(uuid.toString() + "." + RECEIVE_HOLOGRAMS_SOUNDS, value);
    }

    public static boolean getReceiveHUDInCombat(UUID uuid) {
        if (config.contains(uuid.toString() + "." + RECEIVE_HUD_IN_COMBAT)) {
            return config.getBoolean(uuid + "." + RECEIVE_HUD_IN_COMBAT);
        } else {
            setReceiveHologramsSounds(uuid, false);
            return false;
        }
    }

    public static void setReceiveHUDInCombat(UUID uuid, boolean value) {
        config.set(uuid.toString() + "." + RECEIVE_HUD_IN_COMBAT, value);
    }

    public static boolean getReceiveAutoAnnounces(UUID uuid) {
        if (config.contains(uuid.toString() + "." + RECEIVE_AUTO_ANNOUNCES)) {
            return config.getBoolean(uuid + "." + RECEIVE_AUTO_ANNOUNCES);
        } else {
            setReceiveAutoAnnounces(uuid, true);
            return false;
        }
    }

    public static void setReceiveAutoAnnounces(UUID uuid, boolean value) {
        config.set(uuid.toString() + "." + RECEIVE_AUTO_ANNOUNCES, value);
    }

    public static boolean getReceiveParticles(UUID uuid) {
        if (config.contains(uuid.toString() + "." + RECEIVE_PARTICLES)) {
            return config.getBoolean(uuid + "." + RECEIVE_PARTICLES);
        } else {
            setReceiveParticles(uuid, true);
            return false;
        }
    }

    public static void setReceiveParticles(UUID uuid, boolean value) {
        config.set(uuid.toString() + "." + RECEIVE_PARTICLES, value);
    }
}
