/*
 * Copyright (c) 2020-2023.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.guis;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GUIButton {

    private final ItemStack item;

    public GUIButton(ItemStack item) {
        this.item = item;
    }

    /**
     * Get the itemStack of the button
     *
     * @return item
     */
    ItemStack getItem() {
        return this.item;
    }

    /**
     * Call when the player click the button with the event data
     *
     * @param event b event
     */
    public void onClick(InventoryClickEvent event) {
    }

    /**
     * Called when player click the button
     */
    public void onClick() {
    }
}