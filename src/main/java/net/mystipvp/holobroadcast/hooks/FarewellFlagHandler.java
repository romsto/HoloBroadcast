/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.hooks;

import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.SessionManager;
import com.sk89q.worldguard.session.handler.FlagValueChangeHandler;
import com.sk89q.worldguard.session.handler.Handler;
import net.mystipvp.holobroadcast.config.files.SettingsConfig;
import net.mystipvp.holobroadcast.holograms.HologramPlayer;
import net.mystipvp.holobroadcast.holograms.HologramPlayersManager;
import org.bukkit.Bukkit;

public class FarewellFlagHandler extends FlagValueChangeHandler<String> {

    public static final FarewellFlagHandler.Factory FACTORY = new FarewellFlagHandler.Factory();
    public static StringFlag hbflag;

    protected FarewellFlagHandler(Session session) {
        super(session, hbflag);
    }

    public static void sendHUDtoPlayer(LocalPlayer player, String message) {
        HologramPlayer hologramPlayer = HologramPlayersManager.getInstance().getHologramPlayerFromUUID(player.getUniqueId());
        hologramPlayer.showHUD(message, SettingsConfig.getWorldGuardEnterMessageDuration());
    }

    public static void registerFactory() {
        SessionManager sessionManager = WorldGuard.getInstance().getPlatform().getSessionManager();
        sessionManager.registerHandler(FarewellFlagHandler.FACTORY, null);
    }

    public static void registerFlag() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StringFlag flag = new StringFlag("hb-exit-hud", "deny");
            registry.register(flag);
            hbflag = flag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get("hb-exit-hud");
            if (existing instanceof StringFlag)
                hbflag = (StringFlag) existing;
        }
    }

    @Override
    protected void onInitialValue(LocalPlayer localPlayer, ApplicableRegionSet applicableRegionSet, String s) {

    }

    @Override
    protected boolean onSetValue(LocalPlayer localPlayer, Location location, Location location1, ApplicableRegionSet applicableRegionSet, String currentValue, String lastValue, MoveType moveType) {
        if (lastValue != null && !lastValue.equals(currentValue) && Bukkit.getPlayer(localPlayer.getUniqueId()) != null && !lastValue.equals("deny")) {
            sendHUDtoPlayer(localPlayer, lastValue);
        }
        return true;
    }

    @Override
    protected boolean onAbsentValue(LocalPlayer localPlayer, Location location, Location location1, ApplicableRegionSet applicableRegionSet, String lastValue, MoveType moveType) {
        if (lastValue != null && Bukkit.getPlayer(localPlayer.getUniqueId()) != null && !lastValue.equals("deny")) {
            sendHUDtoPlayer(localPlayer, lastValue);
        }
        return true;
    }

    public static class Factory extends Handler.Factory<FarewellFlagHandler> {
        @Override
        public FarewellFlagHandler create(Session session) {
            return new FarewellFlagHandler(session);
        }
    }
}