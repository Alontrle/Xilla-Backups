package net.xilla.backupmaster;

import net.xilla.backupcore.BackupCore;
import net.xilla.backupmaster.command.BackupManager;
import net.xilla.backupmaster.command.StorageManager;
import net.xilla.backupmaster.manager.backup.BackupWorker;
import net.xilla.backupmaster.manager.backup.response.NodeBackupFiles;
import net.xilla.backupmaster.manager.node.request.NodePing;
import net.xilla.backupmaster.manager.node.request.NodeRegister;


public class BackupMaster {

    private static BackupMaster instance;

    public static BackupMaster getInstance() {
        return instance;
    }

    private BackupCore backupCore;
    private BackupWorker backupWorker;

    public static void main(String[] args) {
        new BackupMaster();
    }

    public BackupMaster() {
        this.backupCore = new BackupCore("Master", "Master");

        backupCore.getCommandWorker().start();
        backupCore.getMongoManager().start();

        backupCore.getMongoManager().getClientManager().loadManager(true);

        backupWorker = new BackupWorker();
        backupWorker.start();

        backupCore.getMongoManager().getClientManager().registerHandler(new NodeBackupFiles());

        backupCore.getMongoManager().getRequestManager().registerHandler(new NodePing());
        backupCore.getMongoManager().getRequestManager().registerHandler(new NodeRegister());

        backupCore.getCommandManager().registerCommand(new BackupManager());
        backupCore.getCommandManager().registerCommand(new StorageManager());
    }

    public BackupCore getBackupCore() {
        return backupCore;
    }

    public BackupWorker getBackupWorker() {
        return backupWorker;
    }
}
