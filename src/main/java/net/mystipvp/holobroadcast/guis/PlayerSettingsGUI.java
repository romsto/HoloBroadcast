/*
 * Copyright (c) 2020-2023.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.guis;

import com.cryptomorin.xseries.XSound;
import net.mystipvp.holobroadcast.config.files.MessagesConfig;
import net.mystipvp.holobroadcast.config.files.PlayersDataConfig;
import net.mystipvp.holobroadcast.holograms.HologramPlayer;
import net.mystipvp.holobroadcast.holograms.HologramPlayersManager;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerSettingsGUI {

    public static void open(Player player) {
        HologramPlayer hologramPlayer = HologramPlayersManager.getInstance().getHologramPlayerFromUUID(player.getUniqueId());
        GUIMenu menu = new GUIMenu(MessagesConfig.getGUIName(), 3);

        setStaticItems(menu);

        if (MessagesConfig.isActivated("hud")) {
            if (!hologramPlayer.receiveHUDMessages()) {
                ItemStack item = MessagesConfig.getItem("hud.disabled.item");
                ItemMeta meta = item.getItemMeta();
                assert meta != null;
                meta.setDisplayName(MessagesConfig.getHudDisabledTitle());
                meta.setLore(MessagesConfig.getHudDisabledLore());
                item.setItemMeta(meta);
                menu.setButton(MessagesConfig.getInteger("hud.row"), MessagesConfig.getInteger("hud.slot"), new GUIButton(item) {
                    @Override
                    public void onClick() {
                        hologramPlayer.setReceiveHUDMessage(true);
                        click(player);
                    }
                });
            } else {
                ItemStack item = MessagesConfig.getItem("hud.enabled.item");
                ItemMeta meta = item.getItemMeta();
                assert meta != null;
                meta.setDisplayName(MessagesConfig.getHudEnabledTitle());
                meta.setLore(MessagesConfig.getHudEnabledLore());
                item.setItemMeta(meta);
                menu.setButton(MessagesConfig.getInteger("hud.row"), MessagesConfig.getInteger("hud.slot"), new GUIButton(item) {
                    @Override
                    public void onClick() {
                        hologramPlayer.setReceiveHUDMessage(false);
                        click(player);
                    }
                });
            }
        }

        if (MessagesConfig.isActivated("hud-distance")) {
            int playerHUDDistance = hologramPlayer.getHUDDistance();
            int min = MessagesConfig.getInteger("hud-distance.distance-min");
            int max = MessagesConfig.getInteger("hud-distance.distance-max");
            int next = playerHUDDistance >= max ? min : playerHUDDistance + 1;
            ItemStack item = MessagesConfig.getItem("hud-distance.items.distance-" + playerHUDDistance);
            ItemMeta meta = item.getItemMeta();
            assert meta != null;
            meta.setDisplayName(MessagesConfig.getHudDistanceTitle());
            meta.setLore(MessagesConfig.getHudDistanceLore(playerHUDDistance, next));
            item.setItemMeta(meta);
            menu.setButton(MessagesConfig.getInteger("hud-distance.row"), MessagesConfig.getInteger("hud-distance.slot"), new GUIButton(item) {
                @Override
                public void onClick() {
                    hologramPlayer.setHUDDistance(next);
                    click(player);
                }
            });
        }

        if (MessagesConfig.isActivated("announcer")) {
            if (!hologramPlayer.getReceiveAutoAnnounces()) {
                ItemStack item = MessagesConfig.getItem("announcer.disabled.item");
                ItemMeta meta = item.getItemMeta();
                assert meta != null;
                meta.setDisplayName(MessagesConfig.getAnnouncerDisabledTitle());
                meta.setLore(MessagesConfig.getAnnouncerDisabledLore());
                item.setItemMeta(meta);
                menu.setButton(MessagesConfig.getInteger("announcer.row"), MessagesConfig.getInteger("announcer.slot"), new GUIButton(item) {
                    @Override
                    public void onClick() {
                        hologramPlayer.setReceiveAutoAnnounces(true);
                        click(player);
                    }
                });
            } else {
                ItemStack item = MessagesConfig.getItem("announcer.enabled.item");
                ItemMeta meta = item.getItemMeta();
                assert meta != null;
                meta.setDisplayName(MessagesConfig.getAnnouncerEnabledTitle());
                meta.setLore(MessagesConfig.getAnnouncerEnabledLore());
                item.setItemMeta(meta);
                menu.setButton(MessagesConfig.getInteger("announcer.row"), MessagesConfig.getInteger("announcer.slot"), new GUIButton(item) {
                    @Override
                    public void onClick() {
                        hologramPlayer.setReceiveAutoAnnounces(false);
                        click(player);
                    }
                });
            }
        }

        if (MessagesConfig.isActivated("sounds")) {
            if (!PlayersDataConfig.getReceiveHologramsSounds(player.getUniqueId())) {
                ItemStack item = MessagesConfig.getItem("sounds.disabled.item");
                ItemMeta meta = item.getItemMeta();
                assert meta != null;
                meta.setDisplayName(MessagesConfig.getSoundsDisabledTitle());
                meta.setLore(MessagesConfig.getSoundsDisabledLore());
                item.setItemMeta(meta);
                menu.setButton(MessagesConfig.getInteger("sounds.row"), MessagesConfig.getInteger("sounds.slot"), new GUIButton(item) {
                    @Override
                    public void onClick() {
                        hologramPlayer.setReceiveHologramsSounds(true);
                        click(player);
                    }
                });
            } else {
                ItemStack item = MessagesConfig.getItem("sounds.enabled.item");
                ItemMeta meta = item.getItemMeta();
                assert meta != null;
                meta.setDisplayName(MessagesConfig.getSoundsEnabledTitle());
                meta.setLore(MessagesConfig.getSoundsEnabledLore());
                item.setItemMeta(meta);
                menu.setButton(MessagesConfig.getInteger("sounds.row"), MessagesConfig.getInteger("sounds.slot"), new GUIButton(item) {
                    @Override
                    public void onClick() {
                        hologramPlayer.setReceiveHologramsSounds(false);
                        click(player);
                    }
                });
            }
        }

        if (MessagesConfig.isActivated("combat")) {
            if (!PlayersDataConfig.getReceiveHUDInCombat(player.getUniqueId())) {
                ItemStack item = MessagesConfig.getItem("combat.disabled.item");
                ItemMeta meta = item.getItemMeta();
                assert meta != null;
                meta.setDisplayName(MessagesConfig.getCombatDisabledTitle());
                meta.setLore(MessagesConfig.getCombatDisabledLore());
                item.setItemMeta(meta);
                menu.setButton(MessagesConfig.getInteger("combat.row"), MessagesConfig.getInteger("combat.slot"), new GUIButton(item) {
                    @Override
                    public void onClick() {
                        hologramPlayer.setReceiveHUDInCombat(true);
                        click(player);
                    }
                });
            } else {
                ItemStack item = MessagesConfig.getItem("combat.enabled.item");
                ItemMeta meta = item.getItemMeta();
                assert meta != null;
                meta.setDisplayName(MessagesConfig.getCombatEnabledTitle());
                meta.setLore(MessagesConfig.getCombatEnabledLore());
                item.setItemMeta(meta);
                menu.setButton(MessagesConfig.getInteger("combat.row"), MessagesConfig.getInteger("combat.slot"), new GUIButton(item) {
                    @Override
                    public void onClick() {
                        hologramPlayer.setReceiveHUDInCombat(false);
                        click(player);
                    }
                });
            }
        }

        if (MessagesConfig.isActivated("particles")) {
            if (!PlayersDataConfig.getReceiveParticles(player.getUniqueId())) {
                ItemStack item = MessagesConfig.getItem("particles.disabled.item");
                ItemMeta meta = item.getItemMeta();
                assert meta != null;
                meta.setDisplayName(MessagesConfig.getParticlesDisabledTitle());
                meta.setLore(MessagesConfig.getParticlesDisabledLore());
                item.setItemMeta(meta);
                menu.setButton(MessagesConfig.getInteger("particles.row"), MessagesConfig.getInteger("particles.slot"), new GUIButton(item) {
                    @Override
                    public void onClick() {
                        hologramPlayer.setReceiveParticles(true);
                        click(player);
                    }
                });
            } else {
                ItemStack item = MessagesConfig.getItem("particles.enabled.item");
                ItemMeta meta = item.getItemMeta();
                assert meta != null;
                meta.setDisplayName(MessagesConfig.getParticlesEnabledTitle());
                meta.setLore(MessagesConfig.getParticlesEnabledLore());
                item.setItemMeta(meta);
                menu.setButton(MessagesConfig.getInteger("particles.row"), MessagesConfig.getInteger("particles.slot"), new GUIButton(item) {
                    @Override
                    public void onClick() {
                        hologramPlayer.setReceiveParticles(false);
                        click(player);
                    }
                });
            }
        }

        menu.pack();
        menu.open(player);
    }

    /**
     * Sets all the static (decorative) items in a GUIMenu
     *
     * @param menu
     */
    public static void setStaticItems(GUIMenu menu) {
        ItemStack item = MessagesConfig.getItem("decoration.item1");
        ItemMeta lbmeta = item.getItemMeta();
        assert lbmeta != null;
        lbmeta.setDisplayName(" ");
        item.setItemMeta(lbmeta);

        ItemStack item1 = MessagesConfig.getItem("decoration.item2");
        ItemMeta bmeta = item1.getItemMeta();
        assert bmeta != null;
        bmeta.setDisplayName(" ");
        item1.setItemMeta(bmeta);

        menu.setButton(0, 0, new GUIButton(item));
        menu.setButton(2, 0, new GUIButton(item));
        menu.setButton(2, 8, new GUIButton(item));
        menu.setButton(0, 8, new GUIButton(item));
        menu.setButton(0, 1, new GUIButton(item1));
        menu.setButton(0, 7, new GUIButton(item1));
        menu.setButton(2, 1, new GUIButton(item1));
        menu.setButton(2, 7, new GUIButton(item1));
        menu.setButton(1, 0, new GUIButton(item1));
        menu.setButton(1, 8, new GUIButton(item1));
    }

    /**
     * Performs some actions that needs to be performed every time
     * a player clicks an inventory.
     *
     * @param player the player who we display the inventory to
     */
    public static void click(Player player) {
        open(player);
        if (MessagesConfig.isSoundClickEnabled()) {
            Sound clickSound = XSound.valueOf(MessagesConfig.getSoundClick()).parseSound();
            assert clickSound != null;
            player.playSound(player.getLocation(), clickSound, 1.0F, 1.0F);
        }
    }
}
