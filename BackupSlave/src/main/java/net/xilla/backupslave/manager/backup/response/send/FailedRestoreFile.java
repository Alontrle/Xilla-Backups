package net.xilla.backupslave.manager.backup.response.send;

import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.response.ResponseObject;
import net.xilla.backupcore.filesystem.file.FileObject;

import java.util.UUID;

public class FailedRestoreFile extends ResponseObject {

    public FailedRestoreFile(FileObject fileObject) {
        super(BackupCore.getInstance().getNodeID(), "Master", "FailedRestoreFile", fileObject.getLocalPath(), UUID.randomUUID().toString());
    }

}
