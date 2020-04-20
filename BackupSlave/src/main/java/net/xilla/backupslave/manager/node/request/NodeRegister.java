package net.xilla.backupslave.manager.node.request;

import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.request.RequestObject;

import java.util.UUID;

public class NodeRegister extends RequestObject {

    public NodeRegister(String tempID) {
        super(tempID, "Master", "NodeRegister", BackupCore.getInstance().getIdentifier(), UUID.randomUUID().toString());
    }

}
