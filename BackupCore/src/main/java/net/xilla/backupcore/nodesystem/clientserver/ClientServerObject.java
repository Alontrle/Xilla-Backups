package net.xilla.backupcore.nodesystem.clientserver;

import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.manager.ManagerObject;
import net.xilla.backupcore.filesystem.file.FileObject;
import net.xilla.backupcore.filesystem.filemap.FileMapObject;
import org.bson.Document;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClientServerObject extends ManagerObject {

    private HashMap<Long, FileMapObject> backups; // FileMaps
    private HashMap<String, FileObject> files; // FileMaps

    public ClientServerObject(String id) {
        super(id, null);
        this.backups = new HashMap<>();
        this.files = new HashMap<>();
    }

    public ClientServerObject(Document document) {
        super(document.getString("id"), null);
        this.backups = new HashMap<>();
        this.files = new HashMap<>();
        for(Document doc : document.getList("backups", Document.class)) {
            FileMapObject fileMapObject = new FileMapObject(doc);
            backups.put(fileMapObject.getBackupTime(), fileMapObject);
        }
        for(FileMapObject fileMapObject : backups.values()) {
            for(Document doc : fileMapObject.getFiles()) {
                FileObject fileObject = new FileObject(doc);
                if(!files.containsKey(fileObject.getKey())) {
                    files.put(fileObject.getKey(), fileObject);
                }
            }
        }
    }

    public void addFile(FileObject fileObject, long backupTime) {
        if(!backups.containsKey(backupTime)) {
            backups.put(backupTime, new FileMapObject(getKey(), new ArrayList<>(), backupTime));
        }
        backups.get(backupTime).addFile(fileObject.getDocument());
        files.put(fileObject.getKey(), fileObject);
    }

    @Override
    public Document toDocument() {

        ArrayList<Document> fileMaps = new ArrayList<>();
        for(FileMapObject fileMapObject : backups.values()) {
            fileMaps.add(fileMapObject.toDocument());
        }

        return new Document()
                .append("id", getKey())
                .append("backups", fileMaps);
    }

    public FileMapObject getBackupByTime(long time) {
        return backups.get(time);
    }

    public FileObject getFileByID(String id) {
        return files.get(id);
    }

    public HashMap<Long, FileMapObject> getBackups() {
        return backups;
    }

    public HashMap<String, FileObject> getFiles() {
        return files;
    }
}
