package net.xilla.backupmaster;

import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.config.Config;
import net.xilla.backupcore.api.config.ConfigManager;
import net.xilla.backupcore.api.database.MongoDocument;
import net.xilla.backupcore.storagesystem.type.FTPServer;
import net.xilla.backupcore.storagesystem.type.SFTPServer;
import net.xilla.backupmaster.command.BackupManager;
import net.xilla.backupmaster.command.StorageManager;
import net.xilla.backupmaster.manager.backup.BackupWorker;
import net.xilla.backupmaster.manager.backup.response.NodeBackupFiles;
import net.xilla.backupmaster.manager.node.request.NodePing;
import net.xilla.backupmaster.manager.node.request.NodeRegister;

import java.util.ArrayList;
import java.util.UUID;


public class Main {

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    private BackupCore backupCore;
    private BackupWorker backupWorker;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        this.backupCore = new BackupCore("Master", "Master");

        backupCore.getCommandWorker().start();
        backupCore.getMongoManager().start();

        backupCore.getMongoManager().getClientManager().loadManager(true);

        backupWorker = new BackupWorker();
        backupWorker.start();

//        SFTPServer sftpServer = new SFTPServer(UUID.randomUUID().toString(), "104.128.60.235", 22, "root", "HD844hg38fg39", new ArrayList<>());
//        backupCore.getStorageServerManager().registerServer(sftpServer);
//        backupCore.getMongoManager().getStorageWorker().addDocument(sftpServer.toDocument());

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
