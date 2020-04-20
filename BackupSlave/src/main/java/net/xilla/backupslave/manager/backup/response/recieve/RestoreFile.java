package net.xilla.backupslave.manager.backup.response.recieve;

import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.Log;
import net.xilla.backupcore.api.database.MongoDocument;
import net.xilla.backupcore.api.database.worker.response.ResponseEventHandler;
import net.xilla.backupcore.filesystem.file.FileObject;
import net.xilla.backupcore.storagesystem.StorageRequest;
import net.xilla.backupcore.storagesystem.StorageServerObject;
import net.xilla.backupcore.storagesystem.type.FTPServer;
import net.xilla.backupcore.storagesystem.type.SFTPServer;
import org.bson.Document;

public class RestoreFile extends ResponseEventHandler {

    @Override
    public void ResponseEventHandler(MongoDocument document) {
        Document doc = document.getDocument();
        if(doc.getString("type").equalsIgnoreCase("Restore")) {

            FileObject fileObject = new FileObject((Document)doc.get("data"));

            StorageServerObject storage = BackupCore.getInstance().getStorageServerManager().getStorageServer();
            if(storage == null) {
                Log.sendMessage(2, "File is referencing invalid storage server. Skipping file for backup!");
                return;
            }

            if(storage.getType().equalsIgnoreCase("FTP")) {
                FTPServer ftpServer = (FTPServer)storage;
                ftpServer.addObject(new StorageRequest("Download", fileObject));
            } else if(storage.getType().equalsIgnoreCase("SFTP")) {
                SFTPServer sftpServer = (SFTPServer)storage;
                sftpServer.addObject(new StorageRequest("Download", fileObject));
            }
        }
    }
}
