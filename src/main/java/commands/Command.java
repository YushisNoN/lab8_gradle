package commands;

import commands.exceptions.NullValueException;

public abstract class Command implements Executable {
    protected String description;
    protected boolean isNeedAllCommands = false;
    protected boolean isNeedArguments = false;
    protected boolean isNeedCollection = false;
    protected int commandArguments = 0;

    @Override
    public String toString() {
        return "Command";
    }

    public boolean getNeededCommands() {
        return this.isNeedAllCommands;
    }

    public boolean getNeededArguments() {
        return this.isNeedArguments;
    }

    public boolean getNeededCollections() {
        return this.isNeedCollection;
    }
    public String getDescription() {
        return "";
    }
}
