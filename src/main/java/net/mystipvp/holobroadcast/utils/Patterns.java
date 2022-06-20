/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.utils;

import java.util.regex.Pattern;

public class Patterns {

    public static final Pattern HEX_COLOR_PATTERN = Pattern.compile("&<(#?)([0-9A-Fa-f]{6})>");
    public static final Pattern PLACEHOLDERS_PATTERN = Pattern.compile("(%bl%)|" +
            "(%nl%)|" +
            "(%item_([A-Z_]+[:]?[0-9]*)%)|" +
            "(%sound_([1-9][0-9]?[0-9]?)[_]([aA-zZ_]+([,][-+]?[0-9]*\\.?[0-9]*[,][-+]?[0-9]*\\.?[0-9]*)?)%)|" +
            "(%particle_([1-9][0-9]?[0-9]?)[_]([aA-zZ_]+)([,]([1-9][0-9]?))?%)");

}
