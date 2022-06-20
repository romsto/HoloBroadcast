/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.holograms;

import com.cryptomorin.xseries.XSound;
import net.mystipvp.holobroadcast.HoloBroadcast;
import net.mystipvp.holobroadcast.config.files.MessagesConfig;
import net.mystipvp.holobroadcast.config.files.PermissionTemplatesConfig;
import net.mystipvp.holobroadcast.config.files.PlayersDataConfig;
import net.mystipvp.holobroadcast.config.files.SettingsConfig;
import net.mystipvp.holobroadcast.events.PlayerReceiveHUDEvent;
import net.mystipvp.holobroadcast.holograms.hologramholders.HologramHUD;
import net.mystipvp.holobroadcast.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class HologramPlayer {

    private final UUID uuid;
    private final Player player;
    private final BukkitTask hologramUpdateTask;
    public HologramHUD hologramHUD;
    public String lastMessage = "nobody";
    private boolean receiveHUDMessage;
    private int hudDistance;
    private boolean receiveHologramsSounds;
    private boolean receiveHUDInCombat;
    private boolean receiveAutoAnnounces;
    private boolean receiveParticles;
    private long lastTag = System.currentTimeMillis() - SettingsConfig.getCombattagTime() * 1000L - 1;

    /**
     * Instanciates a new HologramPlayer from a Bukkit Player's UUID.
     *
     * @param uuid
     */
    public HologramPlayer(UUID uuid) {
        this.uuid = uuid;
        this.player = Bukkit.getPlayer(this.uuid);

        // If the player is not in the config, we set default values
        if (!HoloBroadcast.getPlayersDataConfig().getConfig().contains(uuid.toString())) {
            PlayersDataConfig.setReceiveHudMessage(this.uuid, true);
            PlayersDataConfig.setHUDDistance(this.uuid, MessagesConfig.getInteger("hud-distance.default"));
            PlayersDataConfig.setReceiveHologramsSounds(this.uuid, true);
            PlayersDataConfig.setReceiveHUDInCombat(this.uuid, false);
            PlayersDataConfig.setReceiveAutoAnnounces(this.uuid, true);
            PlayersDataConfig.setReceiveParticles(this.uuid, true);
        }

        // In every cases we retrieve its values from the config
        this.receiveHUDMessage = PlayersDataConfig.getReceiveHUDMessage(this.uuid);
        this.hudDistance = PlayersDataConfig.getHUDDistance(this.uuid);
        this.receiveHologramsSounds = PlayersDataConfig.getReceiveHologramsSounds(this.uuid);
        this.receiveHUDInCombat = PlayersDataConfig.getReceiveHUDInCombat(this.uuid);
        this.receiveAutoAnnounces = PlayersDataConfig.getReceiveAutoAnnounces(this.uuid);
        this.receiveParticles = PlayersDataConfig.getReceiveParticles(this.uuid);


        // Initializes the HologramPlayer's update Thread and keeps a reference to it so we can cancel it when the player is destroyed.
        this.hologramUpdateTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (hologramHUD != null) {
                    hologramHUD.update();
                }
            }
        }.runTaskTimerAsynchronously(HoloBroadcast.getInstance(), 0, 1);
    }

    /**
     * Sets the HologramPlayer's HologramHUD and show it for a specific duration.
     * Also removes the HologramPlayer's current HologramHUD.
     *
     * @param creator  the Sender whose permissions will be checked in order to apply placeholders
     * @param rawData  the raw String data
     * @param lifeTime the time in ticks during which the HUD will be show to the player
     */
    public void showHUD(CommandSender creator, String rawData, long lifeTime) {

        if (SettingsConfig.getDonotdisturbList().contains(player.getName()))
            return;

        for (String s : PermissionTemplatesConfig.getPermissionTemplatesList()) {
            if (rawData.contains("%ptemplate_" + s + "%"))
                rawData = rawData.replaceAll("%ptemplate_" + s + "%", PermissionTemplatesConfig.getPermissionTemplate(s).getMessage(player));
        }

        PlayerReceiveHUDEvent event = new PlayerReceiveHUDEvent(player, rawData, lifeTime, creator);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return;

        lifeTime = event.getDuration();
        rawData = event.getMessage();

        this.deleteHUD();

        if (lifeTime == -1)
            lifeTime = SettingsConfig.getDefaultHologramDuration();

        assert player != null; // We can't use HologramPlayer if the player is offline

        this.hologramHUD = new HologramHUD(creator, player.getLocation(), player, rawData, lifeTime);
        if (this.receiveHUDMessage && !SettingsConfig.getDisabledWorlds().contains(player.getWorld().getName()) && (!this.isInCombat() || this.getReceiveHUDInCombat())) {
            BukkitTask task = Bukkit.getScheduler().runTaskAsynchronously(HoloBroadcast.getInstance(), () -> this.hologramHUD.create());
            // We send a text message if the player doesn't receive holograms or if he is in a world where Holograms are disabled
            // Or if he is combat tagged and he has disabled HUD during combat
        } else {
            player.sendMessage(MessageUtil.format(player, MessagesConfig.getChatBroadcastSeparator()));
            for (String line : this.hologramHUD.rawLines) {
                player.sendMessage(MessageUtil.format(player, line));
            }
            if (this.isInCombat() && !SettingsConfig.getDisabledWorlds().contains(player.getWorld().getName()) && !this.getReceiveHUDInCombat())
                player.sendMessage(MessageUtil.format(player, MessagesConfig.getChatMessageCombattaged()));
            player.sendMessage(MessageUtil.format(player, MessagesConfig.getChatBroadcastSeparator()));
            this.hologramHUD = null;
        }

        if (this.receiveHologramsSounds() && SettingsConfig.getHologramHudSoundsEnabled()) {
            String spawnSoundString = SettingsConfig.getHologramHudSpawnSound();
            XSound.play(player, spawnSoundString);
        }
    }

    /**
     * Sets the HologramPlayer's HologramHUD and show it for a specific duration.
     * Also removes the HologramPlayer's current HologramHUD.
     *
     * @param rawData  the raw String data
     * @param lifeTime the time in ticks during which the HUD will be show to the player
     */
    public void showHUD(String rawData, long lifeTime) {
        this.showHUD(Bukkit.getConsoleSender(), rawData, lifeTime);
    }

    /**
     * Sets the HologramPlayer's HologramHUD after a delay and shows it for a specific duration
     * Also removes the HologramPlayer's current HologramHUD.
     *
     * @param creator  the Sender whose permissions will be checked in order to apply placeholders
     * @param rawData  the raw String data
     * @param lifeTime the time in ticks during which the HUD will be show to the player
     * @param delay    the delay in ticks after which the HUD will be shown to the player
     */
    public void showDelayedHUD(CommandSender creator, String rawData, long lifeTime, long delay) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(HoloBroadcast.getInstance(), () -> {
            if (Bukkit.getPlayer(this.uuid) != null) {
                this.showHUD(creator, rawData, lifeTime);
            }
        }, delay);
    }

    /**
     * Sets the HologramPlayer's HologramHUD after a delay and shows it for a specific duration
     * Also removes the HologramPlayer's current HologramHUD.
     *
     * @param rawData  the raw String data
     * @param lifeTime the time in ticks during which the HUD will be show to the player
     * @param delay    the delay in ticks after which the HUD will be shown to the player
     */
    public void showDelayedHUD(String rawData, long lifeTime, long delay) {
        this.showDelayedHUD(Bukkit.getConsoleSender(), rawData, lifeTime, delay);
    }

    /**
     * Deletes the HologramPlayer's current HologramHUD if he has one.
     */
    public void deleteHUD() {
        if (this.hasHUD()) {
            if (this.receiveHologramsSounds() && SettingsConfig.getHologramHudSoundsEnabled()) {

                assert player != null;

                String spawnSoundString = SettingsConfig.getHologramHudDespawnSound();
                XSound.play(player, spawnSoundString);
            }
            this.hologramHUD.remove();
            this.hologramHUD = null;
        }
    }

    /**
     * Checks if a HologramPlayer currently has a HologramHUD.
     *
     * @return true if the HologramPlayer has a HologramHUD; false otherwise.
     */
    public boolean hasHUD() {
        return this.hologramHUD != null;
    }

    /**
     * @return true if the HologramPlayer can receive HUD messages; false otherwise.
     */
    public boolean receiveHUDMessages() {
        return receiveHUDMessage;
    }

    /**
     * Sets the HologramPlayer's receiveHUDMessage parameter and updates its field in the players_data.yml config.
     *
     * @param receiveHUDMessage
     */
    public void setReceiveHUDMessage(boolean receiveHUDMessage) {
        this.receiveHUDMessage = receiveHUDMessage;
        PlayersDataConfig.setReceiveHudMessage(this.uuid, this.receiveHUDMessage);
    }

    /**
     * @return the HologramPlayer's hudDistance.
     */
    public int getHUDDistance() {
        return this.hudDistance;
    }

    /**
     * Sets the HologramPlayer's hudDistance parameter and updates its field in the players_data.yml config.
     *
     * @param hudDistance
     */
    public void setHUDDistance(int hudDistance) {
        this.hudDistance = hudDistance;
        PlayersDataConfig.setHUDDistance(this.uuid, this.hudDistance);
    }

    /**
     * @return true if the player can receive holograms sounds; false otherwise.
     */
    public boolean receiveHologramsSounds() {
        return this.receiveHologramsSounds;
    }

    /**
     * Sets the HologramPlayer's receiveHologramsSounds parameter and updates its field in the players_data.yml config.
     *
     * @param receiveHologramsSounds
     */
    public void setReceiveHologramsSounds(boolean receiveHologramsSounds) {
        this.receiveHologramsSounds = receiveHologramsSounds;
        PlayersDataConfig.setReceiveHologramsSounds(this.uuid, this.receiveHologramsSounds);
    }

    /**
     * @return true if the player can receive HUD while in combattag
     */
    public boolean getReceiveHUDInCombat() {
        return receiveHUDInCombat;
    }

    /**
     * Sets the HologramPlayer's receiveHUDinCombat parameter ad updates its field i the players_data.yml config.
     *
     * @param receiveHUDInCombat
     */
    public void setReceiveHUDInCombat(boolean receiveHUDInCombat) {
        this.receiveHUDInCombat = receiveHUDInCombat;
        PlayersDataConfig.setReceiveHUDInCombat(this.uuid, this.receiveHUDInCombat);
    }

    /**
     * @return true if the player can receive Auto Announces
     */
    public boolean getReceiveAutoAnnounces() {
        return receiveAutoAnnounces;
    }

    /**
     * Sets the HologramPlayer's receiveAutoAnnounces parameter ad updates its field i the players_data.yml config.
     *
     * @param receiveAutoAnnounces
     */
    public void setReceiveAutoAnnounces(boolean receiveAutoAnnounces) {
        this.receiveAutoAnnounces = receiveAutoAnnounces;
        PlayersDataConfig.setReceiveAutoAnnounces(this.uuid, this.receiveAutoAnnounces);
    }

    /**
     * @return true if the player can receive Particles
     */
    public boolean getReceiveParticles() {
        return receiveParticles;
    }

    /**
     * Sets the HologramPlayer's receiveParticles parameter ad updates its field i the players_data.yml config.
     *
     * @param receiveParticles
     */
    public void setReceiveParticles(boolean receiveParticles) {
        this.receiveParticles = receiveParticles;
        PlayersDataConfig.setReceiveParticles(this.uuid, this.receiveParticles);
    }

    /**
     * Clears all of the current HologramPlayer's holograms. This method clears
     * the HologramHUD if the HologramPlayer has one and all of his other HologramHolders.
     */
    public void clearHolograms() {
        this.deleteHUD();
        // Later updates: clear all other holograms
    }

    /**
     * Destroys all of the HologramPlayer's Holograms, cancels the BukkitTask
     * which was updating its Holograms and saves the players_data.yml config file.
     */
    public void destroy() {
        HoloBroadcast.getPlayersDataConfig().save();
        // Clears the HUD and the others Holograms
        this.clearHolograms();

        // Cancels the update Thread
        this.hologramUpdateTask.cancel();
    }

    /**
     * Updates the last time damage taken/given to a player
     */
    public void updateCombatTag() {
        this.lastTag = System.currentTimeMillis();
    }

    /**
     * @return true if the player is considered in combat
     */
    public boolean isInCombat() {
        return System.currentTimeMillis() - lastTag < SettingsConfig.getCombattagTime() * 1000L;
    }
}
