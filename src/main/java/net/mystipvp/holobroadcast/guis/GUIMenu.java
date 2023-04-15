/*
 * Copyright (c) 2020-2023.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.guis;

import net.mystipvp.holobroadcast.HoloBroadcast;
import net.mystipvp.holobroadcast.listeners.GUIListeners;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GUIMenu {

    private final int rows;
    private final Map<Integer, GUIButton> buttons = new ConcurrentHashMap<>();
    private String name;
    private Inventory inventory;

    public GUIMenu(String name, int rows) {
        this.name = name;
        this.rows = rows;
        this.inventory = Bukkit.createInventory(null, rows * 9, name);
    }

    /**
     * Open this GUI to the player
     *
     * @param p player
     */
    public void open(Player p) {
        new BukkitRunnable() {
            @Override
            public void run() {
                p.openInventory(inventory);
                GUIListeners.menuMap.put(p.getUniqueId(), GUIMenu.this);
            }
        }.runTaskLater(HoloBroadcast.getInstance(), 1);
    }

    /**
     * Set all items to the Bukkit Inventory
     */
    public void pack() {
        buttons.keySet().forEach(id -> {
            if (id <= (rows * 9) - 1) {
                inventory.setItem(id, buttons.get(id).getItem());
            }
        });
    }

    /**
     * Called when the player close the inventory
     *
     * @param player player
     */
    public void onClose(Player player) {
    }

    // SETTERS

    /**
     * Set a button to a specific slot
     *
     * @param slot      slot
     * @param guiButton button
     */
    public void setButton(int slot, GUIButton guiButton) {
        buttons.put(slot, guiButton);
    }

    /**
     * Set a button to a specific colon and line
     *
     * @param row       row
     * @param slot      slot
     * @param guiButton button
     */
    public void setButton(int row, int slot, GUIButton guiButton) {
        setButton(row * 9 + slot, guiButton);
    }

    /**
     * Remove a slot button
     *
     * @param slot slot
     */
    private void removeButton(int slot) {
        buttons.remove(slot);
    }


    /**
     * Get inventory name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    // GETTERS

    /**
     * Set the name of the inventory (will update all clients)
     *
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
        List<HumanEntity> viewers = inventory.getViewers();
        this.inventory = Bukkit.createInventory(null, rows * 9, name);
        pack();
        viewers.forEach(player -> open((Player) player));
    }

    /**
     * Get Bukkit inventory
     *
     * @return inv
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Get Button to a specific position
     *
     * @param slot slot
     * @return button
     */
    public GUIButton getButton(int slot) {
        return buttons.get(slot);
    }
}