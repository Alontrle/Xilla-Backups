package net.xilla.backupslave.command;

import net.xilla.backupcore.api.Log;
import net.xilla.backupcore.commandsystem.CommandObject;
import net.xilla.backupslave.BackupSlave;

public class Backup extends CommandObject {

    public Backup() {
        super("Backup", new String[] {"backup"}, "Backup the node.");
    }

    @Override
    public boolean run(String[] args) {
        Log.sendMessage(0, "You have forcefully ran a backup!");
        BackupSlave.getInstance().getBackupWorker().runBackup();
        return true;
    }
}
