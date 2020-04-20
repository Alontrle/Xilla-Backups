package net.xilla.backupmaster.manager.backup.response;

import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.database.MongoDocument;
import net.xilla.backupcore.api.database.worker.client.ClientEventHandler;
import net.xilla.backupcore.filesystem.file.FileObject;
import net.xilla.backupcore.filesystem.filemap.FileMapObject;
import net.xilla.backupcore.nodesystem.NodeObject;
import net.xilla.backupcore.nodesystem.clientserver.ClientServerObject;
import net.xilla.backupcore.storagesystem.StorageServerManager;
import net.xilla.backupcore.storagesystem.StorageServerObject;
import net.xilla.backupmaster.Main;
import net.xilla.backupmaster.manager.backup.BackupWorker;
import org.bson.Document;

import java.util.HashMap;

public class NodeBackupFiles extends ClientEventHandler {

    public void ClientEventHandler(MongoDocument document) {
        //System.out.println("- received document " + document.getDocument().getString("id") + " - " + document.getMongoID().toHexString());
        ClientServerObject clientServerObject = new ClientServerObject(document.getDocument());

        if(clientServerObject.getKey().endsWith("-pending")) {
            String trueID = clientServerObject.getKey().replace("-pending", "");
            ClientServerObject trueClient = BackupCore.getInstance().getClientServerManager().getServerByID(trueID);
            if (trueClient == null) {
                trueClient = new ClientServerObject(trueID);
                BackupCore.getInstance().getClientServerManager().addClientServer(trueClient);
            }

            NodeObject nodeObject = null;
            for(FileMapObject fileMapObject : clientServerObject.getBackups().values()) {
                for (Document fileDocument : fileMapObject.getFiles()) {

                    String id = fileDocument.getString("id");
                    String localPath = fileDocument.getString("localPath");
                    String remotePath;
                    String remoteServerID;

                    boolean isNew = false;

                    FileObject testObject = trueClient.getFileByID(id);
                    if (testObject == null) {
                        isNew = true;
                        StorageServerObject storageServerObject = BackupCore.getInstance().getStorageServerManager().getStorageServer();
                        remoteServerID = storageServerObject.getID();
                        remotePath = storageServerObject.getUnusedID();

                        Document response = new Document("type", "backup").append("localPath", localPath).append("remotePath", remotePath).append("remoteServerID", remoteServerID);
                        MongoDocument mongoDocument = new MongoDocument(response, "Master", document.getOrigin());
                        BackupCore.getInstance().getMongoManager().getResponseWorker().addDocument(mongoDocument);

                    } else {
                        remoteServerID = testObject.getRemoteServerID();
                        remotePath = testObject.getRemotePath();
                    }

                    FileObject fileObject = new FileObject(id, localPath, remotePath, remoteServerID, fileMapObject.getBackupTime());
                    trueClient.addFile(fileObject, fileMapObject.getBackupTime());


                    nodeObject = BackupCore.getInstance().getNodeManager().getNodeByID(document.getOrigin());
                    nodeObject.addServer(trueClient.getKey());

                    if(isNew) {
                        BackupWorker.registerClientServer(trueClient);
                    } else {
                        BackupWorker.updateClientServer(trueClient);
                    }
                }
            }
            if(nodeObject != null) {
                MongoDocument newDocument = new MongoDocument(nodeObject.toDocument(), BackupCore.getInstance().getNodeID(), BackupCore.getInstance().getNodeID());
                BackupCore.getInstance().getMongoManager().getNodeWorker().replaceDocument(new Document("id", nodeObject.getKey()), newDocument.getFinalDocument());
            }
        }
    }

}
