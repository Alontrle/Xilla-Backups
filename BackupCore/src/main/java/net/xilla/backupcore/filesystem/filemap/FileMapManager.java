package net.xilla.backupcore.filesystem.filemap;

import net.xilla.backupcore.api.manager.ManagerParent;

public class FileMapManager extends ManagerParent {

    public void addFile(FileMapObject fileMapObject) {
        addObject(fileMapObject);
    }

    public void removeFile(String fileMapID) {
        removeObject(fileMapID);
    }

    public FileMapObject getFileMapByID(String id) {
        return (FileMapObject)getCache("key").getObject(id);
    }

}
