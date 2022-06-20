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
import net.mystipvp.holobroadcast.utils.PermissionTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PermissionTemplatesConfig {

    private static final Map<String, PermissionTemplate> cacheList = new HashMap<>();
    public static CustomConfig config = HoloBroadcast.getPermissionTemplatesConfig();
    private static Set<String> templates;

    /**
     * @return all the animation configured
     */
    public static Set<String> getPermissionTemplatesList() {
        if (templates == null || templates.isEmpty())
            templates = config.getConfig().getKeys(false);
        return templates;
    }

    /**
     * @param name Name of the Template in templates.yml
     * @return the template string
     */
    public static PermissionTemplate getPermissionTemplate(String name) {
        name = name.toLowerCase();

        if (!cacheList.containsKey(name)) {
            cacheList.put(name, new PermissionTemplate(name, config));
        }
        return cacheList.get(name);
    }

    /**
     * Reloads the configuration
     */
    public static void refetch() {
        templates = config.getConfig().getKeys(false);
        cacheList.clear();
    }
}
