package net.xilla.backupcore.commandsystem.cmd;

import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.Data;
import net.xilla.backupcore.api.Log;
import net.xilla.backupcore.api.manager.ManagerObject;
import net.xilla.backupcore.commandsystem.CommandObject;

import java.util.ArrayList;

public class Help extends CommandObject {

    public Help() {
        super("Help", new String[] {"help", "?"}, "Sends this message");
    }

    @Override
    public boolean run(String[] args) {
        Log.sendMessage(0, new Data().getLineBreak());
        Log.sendMessage(0, "Commands");
        Log.sendMessage(0, new Data().getLineBreak());

        ArrayList<String> commandLines = new ArrayList<>();

        for(ManagerObject object : BackupCore.getInstance().getCommandManager().getList()) {
            CommandObject commandObject = (CommandObject)object;
            commandLines.add(" > " + commandObject.getActivators()[0] + " - " + commandObject.getDescription());
        }
        for(String line : commandLines)
            Log.sendMessage(0, line);

        Log.sendMessage(0, new Data().getLineBreak());
        return true;
    }



}
