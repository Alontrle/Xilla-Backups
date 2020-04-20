package net.xilla.backupmaster.manager.node.response;

import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.response.ResponseObject;

import java.util.UUID;

public class NodePingback extends ResponseObject {

    public NodePingback(String destination) {
        super(BackupCore.getInstance().getNodeID(), destination, "Pong", "", UUID.randomUUID().toString());
    }

}
