package net.xilla.backupslave.manager.node.request;

import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.request.RequestObject;

import java.util.UUID;

public class NodePing extends RequestObject {

    public NodePing() {
        super(BackupCore.getInstance().getNodeID(), "Master", "Ping", "", UUID.randomUUID().toString());
    }

}
