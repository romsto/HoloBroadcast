/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.utils;

import net.mystipvp.holobroadcast.config.files.SettingsConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum Message {

    PERMISSION_DENIED("&cYou do not have the permission to do this."),
    CONSOLE_NOT_SUPPORTED("&cYou can't execute this command using the console."),
    CONSOLE_BUNGEE_NOT("&eBungee Users Warning! &e&oSending a command through the console won't send the command over other servers. Send it as a player instead!"),
    PLAYER_OFFLINE("&cThis player is offline."),
    WRONG_USAGE("&cWrong usage. Please use&e %usage%"),
    MESSAGE_SENT("&aMessage sent."),
    MESSAGE_CLEARED("&aMessage cleared."),
    SUCCESSFULLY_BROADCAST("&aYou have successfully broadcast the message!"),
    RELOAD_SUCCESS("&aConfig files reloaded."),
    DURATION_TOO_BIG("&cThe duration you entered is too big, it should be smaller than " + SettingsConfig.getMaxHologramDuration() + "."),
    DURATION_NOT_NUMERIC("&cThe duration in ticks must be numeric."),
    TEMPLATE_NOT_FOUND("&cThis template is unknown. Configure it in the &etemplates.yml&c file!"),
    BUNGEE_NO_PLAYERS("&cThis command requires at least one online player!");

    private static final String PREFIX = "&7[&bHolo&9Broadcast&7]&f ";
    private final String message;

    Message(String message) {
        this.message = message;
    }

    /**
     * @return the current Message's String message.
     */
    private String getMessage() {
        return this.message;
    }

    /**
     * @return the current Message's String message.
     */
    public String getMessageWithPrefix() {
        return PREFIX + this.message;
    }

    /**
     * Sends the current message with a prefix to a Player.
     * The message is formatted using one of the two Placeholder APIs.
     *
     * @param player the player who will receive the message
     */
    public void send(Player player) {
        String colored = MessageUtil.color(this.getMessageWithPrefix());
        String formatted = MessageUtil.format(player, colored);
        player.sendMessage(formatted);
    }

    /**
     * Sends the current message with a prefix to a CommandSender.
     * If the sender is a Player the message is formatted using one of the two Placeholder APIs.
     * Otherwise it simply translates the color codes.
     *
     * @param sender the one who executed a command
     */
    public void send(CommandSender sender) {
        String colored = MessageUtil.color(this.getMessageWithPrefix());
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String formatted = MessageUtil.format(player, colored);
            player.sendMessage(formatted);
        } else {
            sender.sendMessage(colored);
        }
    }
}
