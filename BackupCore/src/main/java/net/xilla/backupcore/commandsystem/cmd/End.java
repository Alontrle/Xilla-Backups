package net.xilla.backupcore.commandsystem.cmd;

import net.xilla.backupcore.commandsystem.CommandObject;

public class End extends CommandObject {

    public End() {
        super("End", new String[] {"end", "shutdown", "close", "exit"}, "Closes the program");
    }

    @Override
    public boolean run(String[] args) {
        System.exit(0);
        return true;
    }
}
