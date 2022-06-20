/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.HashSet;
import java.util.Set;

public abstract class SubCommand {

    private final String name;
    private final String description;
    private final Set<String> aliases = new HashSet<>();
    private String possibleArgs;
    //private boolean requirePermission = false;
    private String requiredPermission;
    private int minimumArgs = 0;

    /**
     * Constructor for simple SubCommands (without arguments)
     *
     * @param name
     * @param description
     */
    public SubCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Constructor for SubCommands which require some arguments
     *
     * @param name
     * @param description
     * @param possibleArgs
     * @param minimumArgs
     */
    public SubCommand(String name, String description, String possibleArgs, int minimumArgs) {
        this(name, description);
        this.possibleArgs = possibleArgs;
        this.minimumArgs = minimumArgs;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPossibleArgs() {
        return possibleArgs;
    }

    public int getMinimumArgs() {
        return minimumArgs;
    }

    public boolean requirePermission() {
        return this.requiredPermission != null;
    }

    public String getRequiredPermission() {
        return requiredPermission;
    }

    public void setRequiredPermission(String requiredPermission) {
        this.requiredPermission = requiredPermission;
        //this.requirePermission = true;
    }

    public Set<String> getAliases() {
        return aliases;
    }

    public void addAlias(String alias) {
        aliases.add(alias);
        HoloBroadcastCommand.aliases.put(alias, name);
    }

    public String getAliasesString() {
        StringBuilder re = new StringBuilder();
        for (String alias : getAliases()) {
            re.append(", ").append(alias);
        }
        return re.toString().replaceFirst(", ", "");
    }

    public boolean isNumeric(String str) {
        return str != null && str.matches("[-+]?\\d*\\.?\\d+");
    }

    public abstract boolean execute(CommandSender sender, Command command, String label, String[] args);

}
