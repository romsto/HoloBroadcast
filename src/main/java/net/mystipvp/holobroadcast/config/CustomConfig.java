/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.config;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class CustomConfig {

    private final JavaPlugin plugin;
    private final Logger logger;
    private final String name;
    private final FileConfiguration config;
    private File configFile;

    public CustomConfig(JavaPlugin plugin, String name, boolean updateMissingFields) {
        this.plugin = plugin;
        this.logger = this.plugin.getLogger();
        this.name = name;
        this.configFile = new File(this.plugin.getDataFolder() + "/" + this.name);
        this.saveDefaultConfig();
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
        this.reload();
        if (updateMissingFields == true) {

            Configuration currentConfig = this.getConfig();
            currentConfig.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(this.plugin.getResource(this.name))));
            Configuration defaultConfig = currentConfig.getDefaults();

            // Adds the missing fields
            defaultConfig.getKeys(true).forEach(key -> {
                if (currentConfig.isSet(key) == false) {
                    currentConfig.set(key, defaultConfig.get(key));
                }
            });

            // Removes the old fields
            currentConfig.getKeys(true).forEach(key -> {
                if (defaultConfig.isSet(key) == false) {
                    currentConfig.set(key, null);
                }
            });

        }
        this.save();
    }

    /**
     * @return the FileConfiguration instance
     */
    public FileConfiguration getConfig() {
        if (this.config == null) {
            this.reload();
        }
        return this.config;
    }

    /**
     * Reloads the config.
     */
    public void reload() {
        try {
            //this.config = YamlConfiguration.loadConfiguration(this.configFile);
            this.config.load(this.configFile);
        } catch (InvalidConfigurationException e) {
            this.logger.severe("The configuration " + this.name + " is invalid. " + e.getMessage());
        } catch (FileNotFoundException e) {
            this.logger.severe("The file " + this.name + " was not found. " + e.getMessage());
        } catch (IOException e) {
            this.logger.severe("IOException with config " + this.name + ": " + e.getMessage());
        }
    }

    /**
     * Saves the config.
     */
    public void save() {
        try {
            this.config.save(configFile);
        } catch (IOException e) {
            this.logger.severe("Could not save config to " + this.name + ": " + e.getMessage());
        }
    }

    /**
     * Saves the default config (from the jar) to the data folder
     * if, and only if, the current config file does not exist.
     */
    public void saveDefaultConfig() {
        if (configFile == null) {
            // We create a new file instance so we can save the default config into this file
            configFile = new File(plugin.getDataFolder(), this.name);
        }
        if (!configFile.exists()) {
            // If the filed doesn't exist we create a new one using the plugin's resource file
            plugin.saveResource(this.name, false);
        }
    }
}
