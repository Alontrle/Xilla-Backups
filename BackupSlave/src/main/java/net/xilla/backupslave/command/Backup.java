package net.xilla.backupslave.command;

import net.xilla.backupcore.api.Log;
import net.xilla.backupcore.api.config.Config;
import net.xilla.backupcore.api.config.ConfigManager;
import net.xilla.backupcore.commandsystem.CommandObject;
import net.xilla.backupslave.Main;

public class Backup extends CommandObject {

    public Backup() {
        super("Backup", new String[] {"backup"}, "Backup the node.");
    }

    @Override
    public boolean run(String[] args) {
        Log.sendMessage(0, "You have forcefully ran a backup!");
        Main.getInstance().getBackupWorker().runBackup();
        return true;
    }
}
