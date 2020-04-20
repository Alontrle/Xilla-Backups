package net.xilla.backupcore.nodesystem;

import net.xilla.backupcore.api.manager.ManagerCache;
import net.xilla.backupcore.api.manager.ManagerParent;
import net.xilla.backupcore.filesystem.file.FileObject;

import java.util.UUID;

public class NodeManager extends ManagerParent {

    public NodeManager() {
        addCache("identifier", new ManagerCache());
    }

    public void addNode(NodeObject nodeObject) {
        addObject(nodeObject);
        getCache("identifier").putObject(nodeObject.getIdentifier(), nodeObject);
    }

    public void removeNode(NodeObject nodeObject) {
        getCache("identifier").removeObject(nodeObject.getIdentifier());
        removeObject(nodeObject.getKey());
    }

    public NodeObject getNodeByID(String id) {
        return (NodeObject)getCache("key").getObject(id);
    }

    public NodeObject getNodeByIdentifier(String identifier) {
        return (NodeObject)getCache("identifier").getObject(identifier);
    }

    public String getUnusedID() {
        String uuid = UUID.randomUUID().toString();
        while(getCache("key").isCached(uuid)) {
            uuid = UUID.randomUUID().toString();
        }
        return uuid;
    }
}
