package net.xilla.backupcore.storagesystem;

import net.xilla.backupcore.filesystem.file.FileObject;

public class StorageRequest {

    private String type;
    private FileObject fileObject;

    public StorageRequest(String type, FileObject fileObject) {
        this.type = type;
        this.fileObject = fileObject;
    }

    public String getType() {
        return type;
    }

    public FileObject getFileObject() {
        return fileObject;
    }
}
