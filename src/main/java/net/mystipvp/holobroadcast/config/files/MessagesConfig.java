/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.config.files;

import com.cryptomorin.xseries.XMaterial;
import net.mystipvp.holobroadcast.HoloBroadcast;
import net.mystipvp.holobroadcast.config.CustomConfig;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class MessagesConfig {

    private static final CustomConfig messagesConfig = HoloBroadcast.getMessagesConfig();
    private static final FileConfiguration config = messagesConfig.getConfig();
    private static final String SETTINGS_GUI_SECTION = "player-settings-gui.";
    public static Map<String, ItemStack> itemStacks = new HashMap<>();

    /*
    GETTERS
     */

    public static String getChatBroadcastSeparator() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("chat-broadcast-separator"));
    }

    public static String getChatMessageCombattaged() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("chat-message-combattaged"));
    }

    /*
    Settings GUI section "player-settings-gui."
     */

    public static String getGUIName() {
        return ChatColor.translateAlternateColorCodes('&', config.getString(SETTINGS_GUI_SECTION + "gui-name"));
    }

    public static String getHudEnabledTitle() {
        return ChatColor.translateAlternateColorCodes('&', config.getString(SETTINGS_GUI_SECTION + "hud.enabled.title"));
    }

    public static List<String> getHudEnabledLore() {
        List<String> list = config.getStringList(SETTINGS_GUI_SECTION + "hud.enabled.lore");
        for (int i = 0; i < list.size(); i++) {
            list.set(i, ChatColor.translateAlternateColorCodes('&', list.get(i)));
        }
        return list;
    }


    public static String getHudDisabledTitle() {
        return ChatColor.translateAlternateColorCodes('&', config.getString(SETTINGS_GUI_SECTION + "hud.disabled.title"));
    }

    public static List<String> getHudDisabledLore() {
        List<String> list = config.getStringList(SETTINGS_GUI_SECTION + "hud.disabled.lore");
        for (int i = 0; i < list.size(); i++) {
            list.set(i, ChatColor.translateAlternateColorCodes('&', list.get(i)));
        }
        return list;
    }


    public static String getHudDistanceTitle() {
        return ChatColor.translateAlternateColorCodes('&', config.getString(SETTINGS_GUI_SECTION + "hud-distance.title"));
    }

    public static List<String> getHudDistanceLore(int distance, int next) {
        List<String> lore = config.getStringList(SETTINGS_GUI_SECTION + "hud-distance.lore");
        List<String> newLore = new ArrayList<>();
        lore.forEach(line -> newLore.add(ChatColor.translateAlternateColorCodes('&', line.replaceAll("%distance%", String.valueOf(distance)).replaceAll("%distance_next%", String.valueOf(next)))));
        return newLore;
    }

    public static String getAnnouncerEnabledTitle() {
        return ChatColor.translateAlternateColorCodes('&', config.getString(SETTINGS_GUI_SECTION + "announcer.enabled.title"));
    }

    public static List<String> getAnnouncerEnabledLore() {
        List<String> list = config.getStringList(SETTINGS_GUI_SECTION + "announcer.enabled.lore");
        for (int i = 0; i < list.size(); i++) {
            list.set(i, ChatColor.translateAlternateColorCodes('&', list.get(i)));
        }
        return list;
    }

    public static String getAnnouncerDisabledTitle() {
        return ChatColor.translateAlternateColorCodes('&', config.getString(SETTINGS_GUI_SECTION + "announcer.disabled.title"));
    }

    public static List<String> getAnnouncerDisabledLore() {
        List<String> list = config.getStringList(SETTINGS_GUI_SECTION + "announcer.disabled.lore");
        for (int i = 0; i < list.size(); i++) {
            list.set(i, ChatColor.translateAlternateColorCodes('&', list.get(i)));
        }
        return list;
    }

    public static String getSoundsEnabledTitle() {
        return ChatColor.translateAlternateColorCodes('&', config.getString(SETTINGS_GUI_SECTION + "sounds.enabled.title"));
    }

    public static List<String> getSoundsEnabledLore() {
        List<String> list = config.getStringList(SETTINGS_GUI_SECTION + "sounds.enabled.lore");
        for (int i = 0; i < list.size(); i++) {
            list.set(i, ChatColor.translateAlternateColorCodes('&', list.get(i)));
        }
        return list;
    }

    public static String getSoundsDisabledTitle() {
        return ChatColor.translateAlternateColorCodes('&', config.getString(SETTINGS_GUI_SECTION + "sounds.disabled.title"));
    }

    public static List<String> getSoundsDisabledLore() {
        List<String> list = config.getStringList(SETTINGS_GUI_SECTION + "sounds.disabled.lore");
        for (int i = 0; i < list.size(); i++) {
            list.set(i, ChatColor.translateAlternateColorCodes('&', list.get(i)));
        }
        return list;
    }

    public static String getCombatEnabledTitle() {
        return ChatColor.translateAlternateColorCodes('&', config.getString(SETTINGS_GUI_SECTION + "combat.enabled.title"));
    }

    public static List<String> getCombatEnabledLore() {
        List<String> list = config.getStringList(SETTINGS_GUI_SECTION + "combat.enabled.lore");
        for (int i = 0; i < list.size(); i++) {
            list.set(i, ChatColor.translateAlternateColorCodes('&', list.get(i)));
        }
        return list;
    }

    public static String getCombatDisabledTitle() {
        return ChatColor.translateAlternateColorCodes('&', config.getString(SETTINGS_GUI_SECTION + "combat.disabled.title"));
    }

    public static List<String> getCombatDisabledLore() {
        List<String> list = config.getStringList(SETTINGS_GUI_SECTION + "combat.disabled.lore");
        for (int i = 0; i < list.size(); i++) {
            list.set(i, ChatColor.translateAlternateColorCodes('&', list.get(i)));
        }
        return list;
    }

    public static String getParticlesEnabledTitle() {
        return ChatColor.translateAlternateColorCodes('&', config.getString(SETTINGS_GUI_SECTION + "particles.enabled.title"));
    }

    public static List<String> getParticlesEnabledLore() {
        List<String> list = config.getStringList(SETTINGS_GUI_SECTION + "particles.enabled.lore");
        for (int i = 0; i < list.size(); i++) {
            list.set(i, ChatColor.translateAlternateColorCodes('&', list.get(i)));
        }
        return list;
    }

    public static String getParticlesDisabledTitle() {
        return ChatColor.translateAlternateColorCodes('&', config.getString(SETTINGS_GUI_SECTION + "particles.disabled.title"));
    }

    public static List<String> getParticlesDisabledLore() {
        List<String> list = config.getStringList(SETTINGS_GUI_SECTION + "particles.disabled.lore");
        for (int i = 0; i < list.size(); i++) {
            list.set(i, ChatColor.translateAlternateColorCodes('&', list.get(i)));
        }
        return list;
    }

    public static ItemStack getItem(String ref) {
        if (!itemStacks.containsKey(ref)) {
            Optional<XMaterial> opt = XMaterial.matchXMaterial(config.getString(SETTINGS_GUI_SECTION + ref));
            if (opt.isPresent()) {
                itemStacks.put(ref, opt.get().parseItem());
            } else {
                itemStacks.put(ref, XMaterial.STONE.parseItem());
            }
        }
        return itemStacks.get(ref).clone();
    }

    public static int getInteger(String ref) {
        return config.getInt(SETTINGS_GUI_SECTION + ref);
    }

    public static boolean isActivated(String ref) {
        return config.getBoolean(SETTINGS_GUI_SECTION + ref + ".activated");
    }

    public static String getSoundClick() {
        return config.getString(SETTINGS_GUI_SECTION + "clicksound.sound");
    }

    public static boolean isSoundClickEnabled() {
        return config.getBoolean(SETTINGS_GUI_SECTION + "clicksound.enabled");
    }
}
