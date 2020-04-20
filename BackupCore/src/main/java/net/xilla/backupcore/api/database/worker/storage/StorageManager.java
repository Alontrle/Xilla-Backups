package net.xilla.backupcore.api.database.worker.storage;

import com.mongodb.client.MongoCollection;
import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.database.MongoDocument;
import net.xilla.backupcore.api.worker.Worker;
import net.xilla.backupcore.nodesystem.clientserver.ClientServerObject;
import net.xilla.backupcore.storagesystem.StorageServerObject;
import net.xilla.backupcore.storagesystem.type.FTPServer;
import net.xilla.backupcore.storagesystem.type.SFTPServer;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class StorageManager {

    private MongoCollection<Document> storageServers;

    public StorageManager() {
        this.storageServers = BackupCore.getInstance().getMongo().getStorageServers();
    }

    public void loadManager() {
        for (Document doc : storageServers.find()) {
            StorageServerObject storageServer = null;

            if(doc.getString("type").equalsIgnoreCase("SFTP")) {
                storageServer = new SFTPServer(doc);
            } else if(doc.getString("type").equalsIgnoreCase("FTP")) {
                storageServer = new FTPServer(doc);
            }

            if(storageServer != null) {
                BackupCore.getInstance().getStorageServerManager().registerServer(storageServer);
                storageServer.start();
            }
        }
    }
}
