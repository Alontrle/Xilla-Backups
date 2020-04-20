package net.xilla.backupcore.commandsystem.cmd;

import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.commandsystem.CommandObject;

public class Clear extends CommandObject {

    public Clear() {
        super("Clear", new String[] {"clear"}, "Clears the database (ERASES ALL DATA)");
    }

    @Override
    public boolean run(String[] args) {
        BackupCore.getInstance().getMongo().clear();
        return true;
    }
}
