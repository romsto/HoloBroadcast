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
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class SettingsConfig {

    public static final String WELCOME_MESSAGE_SECTION = "welcome-message.";
    public static final String AUTO_ANNOUNCER_SECTION = "auto-announcer.";


    /*
    Misc getters
     */
    public static final String PRIVATE_MESSAGES_SECTION = "private-messages.";
    private static final CustomConfig settings = HoloBroadcast.getSettingsConfig();
    private static final FileConfiguration config = settings.getConfig();
    private static final String JOIN_MESSAGE_SECTION = "join-message.";
    /*
    Hooks section
     */
    private static final String HOOKS_SECTION = "hooks.";
    private static final String PLOT_SECTION = "plotsquared.";
    private static final String WORLDGUARD_SECTION = "worldguard.";

    public static int getDefaultHologramDuration() {
        return config.getInt("default-hologram-duration");
    }

    public static int getMaxHologramDuration() {
        return config.getInt("max-hologram-duration");
    }

    public static boolean getLockHUDY() {
        return config.getBoolean("lock-hud-y");
    }

    public static Location getRelativeLocationDisplay(World world) {
        double x = config.getDouble("relative-display.x");
        double y = config.getDouble("relative-display.y");
        double z = config.getDouble("relative-display.z");
        return new Location(world, x, y, z);
    }

    public static boolean getHologramHudSoundsEnabled() {
        return config.getBoolean("hologram-HUD-sounds-spawn-despawn-enabled");
    }

    public static String getHologramHudSpawnSound() {
        return config.getString("hologram-HUD-spawn-sound");
    }

    public static String getHologramHudDespawnSound() {
        return config.getString("hologram-HUD-despawn-sound");
    }

    public static int getCombattagTime() {
        return config.getInt("combattag-time");
    }

    public static boolean isUsingBungeeMessaging() {
        return config.getBoolean("communicate-through-bungee");
    }

    public static boolean getPlotEnterEnabled() {
        return config.getBoolean(HOOKS_SECTION + PLOT_SECTION + "hud-on-enter-plot.enabled");
    }

    public static long getPlotEnterMessageDuration() {
        return config.getLong(HOOKS_SECTION + PLOT_SECTION + "hud-on-enter-plot.duration");
    }

    public static String getPlotEnterMessageText() {
        return config.getString(HOOKS_SECTION + PLOT_SECTION + "hud-on-enter-plot.message");
    }

    public static boolean isServerPlotDisable() {
        return config.getBoolean(HOOKS_SECTION + PLOT_SECTION + "disable-on-serverplot");
    }

    public static List<String> getPlotOwnersDisableList() {
        return config.getStringList(HOOKS_SECTION + PLOT_SECTION + "disable-for-owners");
    }

    public static long getWorldGuardEnterMessageDuration() {
        return config.getLong(HOOKS_SECTION + WORLDGUARD_SECTION + "hud-on-enter-region.duration");
    }

    public static boolean getWorldGuardActive() {
        return config.getBoolean(HOOKS_SECTION + WORLDGUARD_SECTION + "enabled");
    }

    /*
    Join message section "join-message."
     */

    public static boolean getJoinMessageEnabled() {
        return config.getBoolean(JOIN_MESSAGE_SECTION + "enabled");
    }

    /*
    Welcome message section "welcome-message."
     */

    public static String getJoinMessageText() {
        return config.getString(JOIN_MESSAGE_SECTION + "message");
    }

    public static long getJoinMessageDuration() {
        return config.getLong(JOIN_MESSAGE_SECTION + "duration");
    }

    public static int getJoinSendDelay() {
        return config.getInt(JOIN_MESSAGE_SECTION + "send-delay");
    }

    public static boolean getWelcomeMessageEnabled() {
        return config.getBoolean(WELCOME_MESSAGE_SECTION + "enabled");
    }

    public static String getWelcomeMessageText() {
        return config.getString(WELCOME_MESSAGE_SECTION + "message");
    }

    public static long getWelcomeMessageDuration() {
        return config.getLong(WELCOME_MESSAGE_SECTION + "duration");
    }

    /*
    Announce message section "auto-announcer."
     */

    public static int getWelcomeSendDelay() {
        return config.getInt(WELCOME_MESSAGE_SECTION + "send-delay");
    }

    /*
    List getters
     */
    public static List<String> getDisabledWorlds() {
        return config.getStringList("disabled-worlds");
    }

    public static boolean getAutoAnnouncerEnabled() {
        return config.getBoolean(AUTO_ANNOUNCER_SECTION + "enabled");
    }

    public static long getAutoAnnouncerDuration() {
        return config.getLong(AUTO_ANNOUNCER_SECTION + "duration");
    }

    public static long getAutoAnnouncerPeriod() {
        return config.getLong(AUTO_ANNOUNCER_SECTION + "repeat-each");
    }

    /*
    Private Messages section "private-messages."
     */

    public static List<String> getAutoAnnouncerMessages() {
        return config.getStringList(AUTO_ANNOUNCER_SECTION + "messages");
    }

    public static long getPMDuration() {
        return config.getLong(PRIVATE_MESSAGES_SECTION + "duration");
    }

    public static String getPMMessages() {
        return config.getString(PRIVATE_MESSAGES_SECTION + "template");
    }

    /*
    Others
     */

    public static List<String> getDonotdisturbList() {
        return config.getStringList("disable-hud-for-these-players");
    }
}