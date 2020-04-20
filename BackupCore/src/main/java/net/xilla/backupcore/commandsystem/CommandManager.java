package net.xilla.backupcore.commandsystem;

import net.xilla.backupcore.api.Log;
import net.xilla.backupcore.api.manager.ManagerCache;
import net.xilla.backupcore.api.manager.ManagerParent;

import java.util.Arrays;

public class CommandManager extends ManagerParent {

    private ManagerCache managerCache;

    public CommandManager() {
        managerCache = new ManagerCache();
        addCache("activators", managerCache);
    }

    public void registerCommand(CommandObject commandObject) {
        addObject(commandObject);
        for(String activator : commandObject.getActivators())
            managerCache.putObject(activator.toLowerCase(), commandObject);
    }

    public void runCommand(String input) {
        String command = input.split(" ")[0];
        String[] args = Arrays.copyOfRange(input.split(" "), 1, input.split(" ").length);

        if(managerCache.isCached(command)) {
            CommandObject commandObject = (CommandObject)managerCache.getObject(command);
            if(!commandObject.run(args)) {
                Log.sendMessage(2, "Command registered, but not running properly.");
            }
        } else {
            Log.sendMessage(2, "Unknown command, type \"?\" for a list of available commands.");
        }
    }

}
