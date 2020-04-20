package net.xilla.backupcore.filesystem.file;

import net.xilla.backupcore.api.manager.ManagerObject;
import org.bson.Document;

public class FileObject extends ManagerObject {

    private String localPath;
    private String remotePath;
    private String remoteServerID;
    private long lastUpdated;

    public FileObject(String id, String localPath, String remotePath, String remoteServerID, long lastUpdated) {
        super(id, null);
        this.localPath = localPath;
        this.remotePath = remotePath;
        this.remoteServerID = remoteServerID;
        this.lastUpdated = lastUpdated;
    }

    public FileObject(Document document) {
        super(document.getString("id"), null);
        this.localPath = document.getString("localPath");
        this.remotePath = document.getString("remotePath");
        this.remoteServerID = document.getString("remoteServerID");
        this.lastUpdated = document.getLong("lastUpdated");
    }

    public Document getDocument() {
        return new Document()
                .append("id", getKey())
                .append("localPath", localPath)
                .append("remotePath", remotePath)
                .append("remoteServerID", remoteServerID)
                .append("lastUpdated", lastUpdated);
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public String getLocalPath() {
        return localPath;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public String getRemoteServerID() {
        return remoteServerID;
    }
}
