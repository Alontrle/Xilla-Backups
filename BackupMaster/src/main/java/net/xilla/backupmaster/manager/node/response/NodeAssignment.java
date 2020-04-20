package net.xilla.backupmaster.manager.node.response;

import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.response.ResponseObject;

import java.util.UUID;

public class NodeAssignment extends ResponseObject {

    public NodeAssignment(String destination) {
        super(BackupCore.getInstance().getNodeID(), destination, "NodeAssignment", BackupCore.getInstance().getNodeManager().getUnusedID(), UUID.randomUUID().toString());
    }
}
