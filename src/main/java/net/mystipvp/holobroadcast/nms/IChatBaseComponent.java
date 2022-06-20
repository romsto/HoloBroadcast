/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.nms;

import net.mystipvp.holobroadcast.utils.Patterns;
import net.mystipvp.holobroadcast.utils.Tuple;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;

public class IChatBaseComponent {

    public static final Class<?> IChatBaseComponent = ReflectionUtil.getNMSClass("IChatBaseComponent", "network.chat");
    private static final Logger logger = Logger.getLogger(IChatBaseComponent.class.getName());
    private static Method newIChatBaseComponent = null;

    static {
        try {
            newIChatBaseComponent = IChatBaseComponent.getDeclaredClasses()[0].getMethod("a", String.class);
        } catch (NoSuchMethodException e) {
            logger.log(Level.SEVERE, "An error occurred while initializing IChatBaseComponent.");
        }
    }

    public static Object of(String string) throws InvocationTargetException, IllegalAccessException {
        if (string == null || string.length() <= 0)
            return newIChatBaseComponent.invoke(null, "[\"\",{\"text\": \"\"}]");

        string = string.replaceAll("\"", "'");

        if (!ReflectionCache.supportsHex)
            return newIChatBaseComponent.invoke(null, "[\"\",{\"text\": \"" + string + "\"}]");

        Matcher matcher = Patterns.HEX_COLOR_PATTERN.matcher(string);
        int length = string.length();
        int lastOccurence = 0;
        String lastColor = "#FFFFFF";
        ArrayList<Tuple<String, String>> messages = new ArrayList<>();

        while (matcher.find()) {
            String color = "#" + matcher.group(2);
            int index = matcher.start();
            if (index > 0) {
                messages.add(Tuple.of(string.substring(lastOccurence, index), lastColor));
            }
            lastOccurence = matcher.end();
            lastColor = color;
        }
        if (lastOccurence < length)
            messages.add(Tuple.of(string.substring(lastOccurence), lastColor));

        StringBuilder text = new StringBuilder("[\"\"");
        for (Tuple<String, String> message : messages)
            text.append(",{\"text\": \"").append(message.getFirst()).append("\", \"color\": \"").append(message.getSecond()).append("\"}");
        text.append("]");

        return newIChatBaseComponent.invoke(null, text.toString());
    }

    public static Object direct(String string) throws InvocationTargetException, IllegalAccessException {
        return newIChatBaseComponent.invoke(null, string);
    }

}