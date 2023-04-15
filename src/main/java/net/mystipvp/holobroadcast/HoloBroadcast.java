/*
 * Copyright (c) 2020-2023.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast;

import net.mystipvp.holobroadcast.bungeecord.BungeeChanneling;
import net.mystipvp.holobroadcast.commands.HoloBroadcastCommand;
import net.mystipvp.holobroadcast.config.CustomConfig;
import net.mystipvp.holobroadcast.config.files.SchedulerConfig;
import net.mystipvp.holobroadcast.config.files.TemplatesConfig;
import net.mystipvp.holobroadcast.holograms.HologramPlayersManager;
import net.mystipvp.holobroadcast.hooks.PlaceholdersHook;
import net.mystipvp.holobroadcast.hooks.PlotSquaredHook;
import net.mystipvp.holobroadcast.hooks.WorldGuardHook;
import net.mystipvp.holobroadcast.listeners.CombatListeners;
import net.mystipvp.holobroadcast.listeners.ConnectionListeners;
import net.mystipvp.holobroadcast.listeners.GUIListeners;
import net.mystipvp.holobroadcast.listeners.MovementsListeners;
import net.mystipvp.holobroadcast.nms.ReflectionCache;
import net.mystipvp.holobroadcast.utils.AutoAnnouncer;
import net.mystipvp.holobroadcast.utils.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class HoloBroadcast extends JavaPlugin implements Listener {

    public static final String user = "%%__USER__%%",
            resource = "%%__RESOURCE__%%",
            uid = "%%__NONCE__%%";
    public static String VERSION;
    private static HoloBroadcast instance;
    private static PlaceholdersHook placeholdersHook;
    private static CustomConfig settingsConfig, dataConfig, animationsConfig, messagesConfig, templatesConfig, permissiontemplatesConfig, schedulerConfig;

    /**
     * @return the HoloBroadcast JavaPlugin instance
     */
    public static HoloBroadcast getInstance() {
        return instance;
    }

    /**
     * @return the Placeholders hook
     */
    public static PlaceholdersHook getPlaceholdersHook() {
        return placeholdersHook;
    }

    /**
     * @return the settings.yml CustomConfig.
     */
    public static CustomConfig getSettingsConfig() {
        return settingsConfig;
    }

    /**
     * @return the players_data.yml CustomConfig.
     */
    public static CustomConfig getPlayersDataConfig() {
        return dataConfig;
    }

    /**
     * @return the animations.yml CustomConfig.
     */
    public static CustomConfig getAnimationsConfig() {
        return animationsConfig;
    }

    /**
     * @return the gui_settings.yml CustomConfig.
     */
    public static CustomConfig getMessagesConfig() {
        return messagesConfig;
    }

    /**
     * @return the scheduler.yml CustomConfig.
     */
    public static CustomConfig getSchedulerConfig() {
        return schedulerConfig;
    }

    /**
     * @return the templates.yml CustomConfig.
     */
    public static CustomConfig getTemplatesConfig() {
        return templatesConfig;
    }

    /**
     * @return the permission_templates.yml CustomConfig.
     */
    public static CustomConfig getPermissionTemplatesConfig() {
        return permissiontemplatesConfig;
    }

    @Override
    public void onLoad() {
        instance = this;

        WorldGuardHook.init();
    }

    @Override
    public void onEnable() {
        instance = this;

        settingsConfig = new CustomConfig(this, "settings.yml", true);
        dataConfig = new CustomConfig(this, "players_data.yml", false);
        animationsConfig = new CustomConfig(this, "animations.yml", false);
        messagesConfig = new CustomConfig(this, "gui_settings.yml", true);
        templatesConfig = new CustomConfig(this, "templates.yml", false);
        permissiontemplatesConfig = new CustomConfig(this, "permission_templates.yml", false);
        schedulerConfig = new CustomConfig(this, "scheduler.yml", false);

        Logger logger = Bukkit.getLogger();

        // Hooks
        placeholdersHook = new PlaceholdersHook();
        new PlotSquaredHook();
        new BungeeChanneling();
        WorldGuardHook.load();

        // Register templates
        TemplatesConfig.getTemplatesList();

        // Register schedulers
        SchedulerConfig.refetch();

        HologramPlayersManager hologramPlayersManager = HologramPlayersManager.getInstance();

        // Command registration
        PluginCommand pluginCommand = getCommand("holobroadcast");

        if (pluginCommand == null) {
            logger.warning("Couldn't load holobroadcast command. Stopping the plugin.");
            onDisable();
            return;
        }

        pluginCommand.setExecutor(HoloBroadcastCommand.getInstance());
        pluginCommand.setTabCompleter(HoloBroadcastCommand.getInstance());

        // Events registration
        Bukkit.getPluginManager().registerEvents(new ConnectionListeners(), this);
        Bukkit.getPluginManager().registerEvents(new GUIListeners(), this);
        Bukkit.getPluginManager().registerEvents(new CombatListeners(), this);
        Bukkit.getPluginManager().registerEvents(new MovementsListeners(), this);

        // BStats Metrics
        new Metrics(this, 7447);

        // Instantiates HologramPlayers for each online Bukkit Player
        Bukkit.getOnlinePlayers().forEach(o -> hologramPlayersManager.createHologramPlayer(o.getUniqueId()));

        VERSION = getDescription().getVersion();

        logger.info("Loading reflection...");
        boolean ignored = ReflectionCache.setMarker == null;

        AutoAnnouncer.update();
        SchedulerConfig.initTask();

        logger.info("O      O    OOAD");
        logger.info("L      L    L    C");
        logger.info("O      O    O    A");
        logger.info("HBROADCH    HAST");
        logger.info("O      O    O    S");
        logger.info("L      L    L    T");
        logger.info("O      O    OBRD");

        logger.info(" ");
        logger.info("Version licenced to: " + user + " - " + uid);
    }

    @Override
    public void onDisable() {
        dataConfig.save();
        settingsConfig.save();
        animationsConfig.save();
        messagesConfig.save();

        HologramPlayersManager hologramPlayersManager = HologramPlayersManager.getInstance();
        // Destroys all online HologramPlayers
        Bukkit.getOnlinePlayers().forEach(o -> hologramPlayersManager.deleteHologramPlayer(o.getUniqueId()));
    }
}
