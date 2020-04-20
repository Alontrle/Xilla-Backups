package net.xilla.backupcore.nodesystem;

import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.database.MongoDocument;
import net.xilla.backupcore.api.manager.ManagerObject;
import org.bson.Document;

import java.util.List;

public class NodeObject extends ManagerObject {

    private long lastPinged;
    private boolean active;
    private String identifier;
    private List<String> clientServerIDs;

    public NodeObject(String nodeID, long lastPinged, List<String> clientServerIDs, String identifier) {
        super(nodeID, null);
        this.lastPinged = lastPinged;
        this.active = (System.currentTimeMillis() - lastPinged) <= 60;
        this.clientServerIDs = clientServerIDs;
        this.identifier = identifier;
    }

    public NodeObject(Document document) {
        super(document.getString("id"), null);
        this.lastPinged = document.getLong("lastPinged");
        this.active = (System.currentTimeMillis() - lastPinged) <= 60;
        this.clientServerIDs = document.getList("clientServerIDs", String.class);
        this.identifier = document.getString("identifier");
    }

    @Override
    public Document toDocument() {
        return new Document()
                .append("id", getKey())
                .append("lastPinged", lastPinged)
                .append("identifier", identifier)
                .append("clientServerIDs", clientServerIDs);
    }

    public String getID() {
        return getKey();
    }

    public boolean isActive() {
        return active;
    }

    public long getLastPinged() {
        return lastPinged;
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<String> getClientServerIDs() {
        return clientServerIDs;
    }

    public void addServer(String id) {
        if(!clientServerIDs.contains(id)) {
            clientServerIDs.add(id);
        }
    }

}
