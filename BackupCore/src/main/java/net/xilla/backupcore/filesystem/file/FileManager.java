package net.xilla.backupcore.filesystem.file;

import net.xilla.backupcore.api.manager.ManagerParent;

public class FileManager extends ManagerParent {

    public void addFile(FileObject fileObject) {
        addObject(fileObject);
    }

    public void removeFile(String fileID) {
        removeObject(fileID);
    }

    public FileObject getFileByID(String id) {
        return (FileObject)getCache("key").getObject(id);
    }

}
