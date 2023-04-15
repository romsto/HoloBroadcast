/*
 * Copyright (c) 2020-2023.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.commands;

import net.mystipvp.holobroadcast.HoloBroadcast;
import net.mystipvp.holobroadcast.utils.Message;
import net.mystipvp.holobroadcast.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class HoloBroadcastCommand implements CommandExecutor, TabCompleter {

    public static final HashMap<String, String> aliases = new HashMap<>();
    private static HoloBroadcastCommand instance;
    private final HashMap<String, SubCommand> subCommands;

    private HoloBroadcastCommand() {
        subCommands = new HashMap<>();

        // Register SubCommands below
        subCommands.put("settings", new SettingsSubCommand());
        subCommands.put("send", new SendSubCommand());
        subCommands.put("clear", new ClearSubCommand());
        subCommands.put("broadcast", new BroadcastSubCommand());
        subCommands.put("broadcastall", new BroadcastAllSubCommand());
        subCommands.put("broadcastduration", new BroadcastDurSubCommand());
        subCommands.put("sendduration", new SendDurationSubCommand());
        subCommands.put("sendtemplate", new SendTemplateSubCommand());
        subCommands.put("broadcasttemplate", new BroadcastTemplateSubCommand());
        subCommands.put("reload", new ReloadSubCommand());
        subCommands.put("reply", new ReplySubCommand());
        subCommands.put("msg", new MessageSubCommand());
    }

    /**
     * @return HoloBroadcastCommand's instance.
     */
    public static HoloBroadcastCommand getInstance() {
        if (instance == null) {
            instance = new HoloBroadcastCommand();
        }
        return instance;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }
        String subCommandLabel = args[0];
        // Help
        if (subCommandLabel.equalsIgnoreCase("help") && args.length >= 2) {
            if (subCommands.containsKey(args[1].toLowerCase())) {
                SubCommand subCommand = subCommands.get(args[1].toLowerCase());
                if (!subCommand.requirePermission() || sender.hasPermission(subCommand.getRequiredPermission())) {
                    sender.sendMessage(MessageUtil.color("&b&l&m      &3&l&m       &9&l&m        &r &b&lHolo&9&lBroadcast&r &9&l&m        &3&l&m       &b&l&m      "));
                    sender.sendMessage("§b§oHelp for sub-command §9" + subCommand.getName());
                    sender.sendMessage(" ");
                    sender.sendMessage("§7" + subCommand.getDescription());
                    sender.sendMessage("§8§oUsage: §r§b/§7hb §b" + subCommand.getName() + " §9" + subCommand.getPossibleArgs());
                    sender.sendMessage("        §7§o(" + subCommand.getAliasesString() + ")");
                    return true;
                }
            }
            sender.sendMessage("§cThe given command is unknown.");
            return true;
        }
        if (subCommandLabel.equalsIgnoreCase("help")) {
            sendHelp(sender);
            return true;
        }

        SubCommand subCommand = subCommands.get(subCommandLabel);
        if (aliases.containsKey(subCommandLabel))
            subCommand = subCommands.get(aliases.get(subCommandLabel));
        if (subCommand != null) {

            // If the player doesn't have the required permission
            if (subCommand.requirePermission()) {
                if (!sender.hasPermission(subCommand.getRequiredPermission())) {
                    Message.PERMISSION_DENIED.send(sender);
                    return true;
                }
            }

            // If the minimum args number is not respected
            if (subCommand.getMinimumArgs() > (args.length - 1)) {
                sender.sendMessage(MessageUtil.color(Message.WRONG_USAGE.getMessageWithPrefix().replaceAll("%usage%", "/hb " + subCommand.getName() + " " + subCommand.getPossibleArgs())));
                return true;
            }
            return subCommand.execute(sender, command, label, Arrays.copyOfRange(args, 1, args.length));

        } else {
            sender.sendMessage(MessageUtil.color(Message.WRONG_USAGE.getMessageWithPrefix().replaceAll("%usage%", "/holobroadcast help")));
        }
        return true;
    }

    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length > 1)
            return null;
        List<String> returning = new ArrayList<>(subCommands.keySet());
        returning.addAll(aliases.keySet());
        returning.add("help");
        returning.removeIf(s -> {
            if (s.equalsIgnoreCase("help"))
                return false;
            SubCommand subCommand = subCommands.get(s);
            if (subCommand == null)
                subCommand = subCommands.get(aliases.get(s));
            return subCommand.requirePermission() && !sender.hasPermission(subCommand.getRequiredPermission());
        });
        if (args[0] == null || args[0].isEmpty())
            return returning;
        returning.removeIf(s -> !s.startsWith(args[0].toLowerCase()));
        return returning;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(MessageUtil.color("&b&l&m      &3&l&m       &9&l&m        &r &b&lHolo&9&lBroadcast&r &9&l&m        &3&l&m       &b&l&m      "));
        sender.sendMessage(MessageUtil.color("&7&oMade by &r&9_Rolyn &r&7&oand &r&9DevKrazy&r&7&o.          Version: &r&7" + HoloBroadcast.VERSION));
        sender.sendMessage(" ");
        AtomicBoolean found = new AtomicBoolean(false);
        subCommands.forEach((s, subCommand) -> {
            // Only displays the commands which the sender has the permission to execute or which doesn't require any permission
            if (!subCommand.requirePermission() || sender.hasPermission(subCommand.getRequiredPermission())) {
                found.set(true);
                sender.sendMessage(MessageUtil.color("  &9/§7hb &b" + subCommand.getName() + " &9" + subCommand.getPossibleArgs()));
            }
        });
        if (!found.get()) {
            sender.sendMessage(MessageUtil.color("&8&oIt seems that you don't have any permission :("));
        } else {
            sender.sendMessage(" ");
            sender.sendMessage("§8§oFor more help, try §b/hb help §9<command>§8§o!");
        }
    }
}
