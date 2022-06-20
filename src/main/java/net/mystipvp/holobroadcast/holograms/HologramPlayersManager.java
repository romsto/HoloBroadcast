/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.holograms;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HologramPlayersManager {

    private static HologramPlayersManager instance;
    private final Map<UUID, HologramPlayer> hologramPlayers = new HashMap<>();

    /**
     * @return the HologramPlayersManager's instance.
     */
    public static HologramPlayersManager getInstance() {
        if (instance == null) {
            instance = new HologramPlayersManager();
        }
        return instance;
    }

    /**
     * Creates a new HologramPlayer and keeps a reference to it.
     * If a HologramPlayer is already associated to the given UUID this method does nothing and returns null.
     *
     * @param uuid the Bukkit Player's UUID.
     * @return the HologramPlayer's instance if it was created; null otherwise.
     */
    public HologramPlayer createHologramPlayer(UUID uuid) {
        if (!hologramPlayers.containsKey(uuid)) {
            HologramPlayer hologramPlayer = new HologramPlayer(uuid);
            hologramPlayers.put(uuid, hologramPlayer);
            return hologramPlayer;
        } else
            return hologramPlayers.get(uuid);
    }

    /**
     * Deletes a HologramPlayer and its reference.
     *
     * @param uuid the Bukkit Player's UUID used to create the HologramPlayer which needs to be deleted.
     */
    public void deleteHologramPlayer(UUID uuid) {
        if (existsHologramPlayer(uuid)) {
            HologramPlayer hologramPlayer = getHologramPlayerFromUUID(uuid);
            hologramPlayer.destroy();
        }
        hologramPlayers.remove(uuid);
    }

    /**
     * Returns a given UUID's associated HologramPlayer. If the UUID has no associated HologramPlayer
     * this method returns null.
     *
     * @param uuid the Bukkit Player's UUID.
     * @return the HologramPlayer's instance if it was found; null otherwise.
     */
    public HologramPlayer getHologramPlayerFromUUID(UUID uuid) {
        return hologramPlayers.get(uuid);
    }

    /**
     * Checks if a HologramPlayer exists.
     *
     * @param uuid the Bukkit Player's UUID used to create the HologramPlayer whose existence we want to check.
     * @return true if a HologramPlayer is associated to this UUID; false otherwise.
     */
    public boolean existsHologramPlayer(UUID uuid) {
        return hologramPlayers.containsKey(uuid);
    }
}
