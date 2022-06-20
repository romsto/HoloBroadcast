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
import net.mystipvp.holobroadcast.holograms.HologramPlayersManager;
import net.mystipvp.holobroadcast.utils.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

public class SchedulerConfig {

    private static final CustomConfig schedulerConfig = HoloBroadcast.getSchedulerConfig();
    private static final FileConfiguration config = schedulerConfig.getConfig();
    private static final HashMap<String, Tuple<String, Long>> cachedEveryDays = new HashMap<>();
    private static final HashMap<String, Tuple<String, Long>> cachedOnetime = new HashMap<>();
    private static final HashMap<Tuple<String, String>, Tuple<String, Long>> cachedSpecifiedDay = new HashMap<>();

    private static final SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat formatDay = new SimpleDateFormat("EEEE", new Locale("en", "US"));
    private static final SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void refetch() {
        cachedEveryDays.clear();
        cachedOnetime.clear();
        cachedSpecifiedDay.clear();
        Set<String> onetime = config.getConfigurationSection("onetime").getKeys(false);
        Set<String> repeat = config.getConfigurationSection("repeat").getKeys(false);

        int loaded = 0;

        for (String task : onetime) {
            try {
                String stringDate = config.getString("onetime." + task + ".date");
                Date date = formatDate.parse(stringDate);
                String message = config.getString("onetime." + task + ".message");
                long duration = config.getLong("onetime." + task + ".duration");
                cachedOnetime.put(stringDate, Tuple.of(message, duration));
                loaded++;
            } catch (Exception e) {
                HoloBroadcast.getInstance().getLogger().warning("Task " + task + " cannot be load. Check the scheduler.yml file.");
            }
        }

        for (String task : repeat) {
            try {
                String stringHour = config.getString("repeat." + task + ".hour");
                Date hour = formatTime.parse(stringHour);
                String message = config.getString("repeat." + task + ".message");
                long duration = config.getLong("repeat." + task + ".duration");
                if (config.getBoolean("repeat." + task + ".everyday")) {
                    cachedEveryDays.put(stringHour, Tuple.of(message, duration));
                } else {
                    String stringDay = config.getString("repeat." + task + ".week-day");
                    Date day = formatDay.parse(stringDay);
                    cachedSpecifiedDay.put(Tuple.of(stringHour, stringDay), Tuple.of(message, duration));
                }
                loaded++;
            } catch (Exception e) {
                HoloBroadcast.getInstance().getLogger().warning("Task " + task + " cannot be load. Check the scheduler.yml file.");
            }
        }

        if (loaded >= 1)
            HoloBroadcast.getInstance().getLogger().info("Loaded " + loaded + " scheduled task" + (loaded > 1 ? "s" : "") + "!");
    }

    public static void checkAndAct() {
        Date current = new Date();
        String currentDay = formatDay.format(current);
        String currentHour = formatTime.format(current);
        String currentDate = formatDate.format(current);

        HologramPlayersManager manager = HologramPlayersManager.getInstance();
        cachedOnetime.forEach((date, stringLongTuple) -> {
            if (date.equalsIgnoreCase(currentDate))
                Bukkit.getOnlinePlayers().forEach(o -> manager.getHologramPlayerFromUUID(o.getUniqueId()).showHUD(Bukkit.getConsoleSender(), stringLongTuple.getFirst(), stringLongTuple.getSecond()));
        });
        cachedEveryDays.forEach((date, stringLongTuple) -> {
            if (date.equalsIgnoreCase(currentHour))
                Bukkit.getOnlinePlayers().forEach(o -> manager.getHologramPlayerFromUUID(o.getUniqueId()).showHUD(Bukkit.getConsoleSender(), stringLongTuple.getFirst(), stringLongTuple.getSecond()));
        });
        cachedSpecifiedDay.forEach((dateTuple, stringLongTuple) -> {
            if (dateTuple.getSecond().equalsIgnoreCase(currentDay) && dateTuple.getFirst().equalsIgnoreCase(currentHour))
                Bukkit.getOnlinePlayers().forEach(o -> manager.getHologramPlayerFromUUID(o.getUniqueId()).showHUD(Bukkit.getConsoleSender(), stringLongTuple.getFirst(), stringLongTuple.getSecond()));
        });
    }

    public static void initTask() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(HoloBroadcast.getInstance(), SchedulerConfig::checkAndAct, 0, 20);
    }
}
