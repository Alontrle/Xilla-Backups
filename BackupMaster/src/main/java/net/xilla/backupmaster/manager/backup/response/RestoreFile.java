package net.xilla.backupmaster.manager.backup.response;

import net.xilla.backupcore.api.response.ResponseObject;
import org.bson.Document;

import java.util.UUID;

public class RestoreFile extends ResponseObject {

    public RestoreFile(String destination, Document file) {
        super("Master", destination, "Restore", file, UUID.randomUUID().toString());
    }

}
