/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.config.files;

import net.mystipvp.holobroadcast.HoloBroadcast;
import net.mystipvp.holobroadcast.animations.Animator;
import net.mystipvp.holobroadcast.config.CustomConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnimationsConfig {

    private static final Map<String, ArrayList<String>> cacheList = new HashMap<>();
    private static final Map<String, Integer> cacheRefresh = new HashMap<>();
    public static CustomConfig config = HoloBroadcast.getAnimationsConfig();
    private static Set<String> animations;

    /**
     * @return all the animation configured
     */
    public static Set<String> getAnimationsList() {
        if (animations == null || animations.isEmpty())
            animations = config.getConfig().getKeys(false);
        return animations;
    }

    /**
     * @param name Name of the Animation in animations.yml
     * @return the animator object new instance
     */
    public static Animator getAnimation(String name) {
        ArrayList<String> list;
        int refreshRate;
        if (cacheList.containsKey(name))
            list = cacheList.get(name);
        else {
            list = new ArrayList<>(config.getConfig().getStringList(name + ".list"));
            cacheList.put(name, list);
        }

        if (cacheRefresh.containsKey(name))
            refreshRate = cacheRefresh.get(name);
        else {
            refreshRate = config.getConfig().getInt(name + ".refresh-rate");
            cacheRefresh.put(name, refreshRate);
        }

        return new Animator(list, refreshRate);
    }

    public static void refetch() {
        cacheList.clear();
        cacheRefresh.clear();
        animations = config.getConfig().getKeys(false);
    }
}
