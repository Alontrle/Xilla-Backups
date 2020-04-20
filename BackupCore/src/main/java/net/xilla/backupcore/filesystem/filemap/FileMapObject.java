package net.xilla.backupcore.filesystem.filemap;

import net.xilla.backupcore.api.manager.ManagerObject;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class FileMapObject extends ManagerObject {

    private List<Document> files;
    private long backupTime;

    public FileMapObject(String id, ArrayList<Document> files, long backupTime) {
        super(id, null);
        this.files = files;
        this.backupTime = backupTime;
    }

    public FileMapObject(Document document) {
        super(document.getString("id"), null);
        this.files = document.getList("fileIDs", Document.class);
        this.backupTime = document.getLong("backupTime");
    }

    @Override
    public Document toDocument() {
        return new Document()
                .append("id", getKey())
                .append("fileIDs", files)
                .append("backupTime", backupTime);
    }

    public long getBackupTime() {
        return backupTime;
    }

    public List<Document> getFiles() {
        return files;
    }

    public void addFile(Document document) {
        files.add(document);
    }

}
