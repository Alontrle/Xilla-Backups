package net.xilla.backupslave;

import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.Data;
import net.xilla.backupcore.api.Log;
import net.xilla.backupcore.api.config.Config;
import net.xilla.backupcore.api.config.ConfigManager;
import net.xilla.backupcore.api.database.MongoDocument;
import net.xilla.backupslave.command.Backup;
import net.xilla.backupslave.manager.backup.BackupWorker;
import net.xilla.backupslave.manager.backup.response.recieve.BackupFile;
import net.xilla.backupslave.manager.backup.response.recieve.RestoreFile;
import net.xilla.backupslave.manager.node.NodeWorker;
import net.xilla.backupslave.manager.node.request.NodeRegister;
import net.xilla.backupslave.manager.node.response.NodeAssignment;
import net.xilla.backupslave.manager.node.response.NodePingback;

import java.util.Collections;
import java.util.Scanner;
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
        instance = this;

        this.backupCore = new BackupCore("slave", "slave");

        Config config = ConfigManager.getInstance().getConfig("settings.json");

        if(!config.toJson().containsKey("node-id")) {
            config.set("node-id", "temp-" + UUID.randomUUID());
            config.save();
        }

        String identifier = config.getString("identifier");
        if(identifier == null) {
            Log.sendMessage(0, "Please give your server a label / identifier : ");
            Scanner scanner = new Scanner(System.in);
            config.set("identifier", scanner.nextLine());
            config.save();
            Log.sendMessage(0,"Your servers label has been set, you can change this in the settings.json file.");
        }

        backupCore.setIdentifier(config.getString("identifier"));
        backupCore.setNodeID(config.getString("node-id"));
        backupCore.getMongoManager().start();
        backupCore.getMongoManager().getResponseManager().registerHandler(new NodeAssignment());
        backupCore.getMongoManager().getResponseManager().registerHandler(new NodePingback());

        String id = config.getString("node-id");
        if(id.startsWith("temp-")) {
            Log.sendMessage(0, "Registering the slave server with the master server. This shouldn't take long...");
            MongoDocument mongoDocument = new MongoDocument(new NodeRegister(config.getString("node-id")).toDocument(), id, "Master");
            backupCore.getMongoManager().getRequestWorker().addDocument(mongoDocument);

            while (config.getString("node-id").startsWith("temp-")) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignore) {}
            }
            Log.sendMessage(0, "Registered!");
        }
        backupCore.setNodeID(config.getString("node-id"));

        config.loadDefault("timezone", "America/New_York");
        config.loadDefault("backup-times", Collections.singletonList("1:00"));
        config.loadDefault("last-backup", 0);
        config.loadDefault("backup-directory", "/srv/daemon-data/");
        config.set("last-backup", 0);
        config.save();

        new NodeWorker().start();

        backupCore.getCommandWorker().start();

        backupCore.getMongoManager().getResponseManager().registerHandler(new BackupFile());
        backupCore.getMongoManager().getResponseManager().registerHandler(new RestoreFile());

        backupCore.getMongoManager().getClientManager().loadManager(false);

        backupCore.getCommandManager().registerCommand(new Backup());

        this.backupWorker = new BackupWorker();
        backupWorker.start();
    }

    public BackupCore getBackupCore() {
        return backupCore;
    }

    public BackupWorker getBackupWorker() {
        return backupWorker;
    }
}