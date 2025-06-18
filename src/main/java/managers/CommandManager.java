package managers;


import commands.Executable;

import java.util.HashMap;
public class CommandManager {
    protected HashMap<String, Executable> commandsMap = new HashMap<>();

    public HashMap<String, Executable> getCommandsList() {
        return commandsMap;
    }

    public void addCommand(Executable command) {
        commandsMap.put(command.toString(), command);
    }

}
