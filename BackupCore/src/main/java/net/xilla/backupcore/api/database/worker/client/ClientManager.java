package net.xilla.backupcore.api.database.worker.client;

import com.mongodb.client.MongoCollection;
import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.database.MongoDocument;
import net.xilla.backupcore.api.worker.Worker;
import net.xilla.backupcore.nodesystem.clientserver.ClientServerObject;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ClientManager extends Worker {

    private MongoCollection<Document> clientServers;
    private List<ClientEventHandler> events;
    private List<String> recentIDs;

    public ClientManager() {
        super(50);
        this.events = new ArrayList<>();
        this.recentIDs = new ArrayList<>();
        this.clientServers = BackupCore.getInstance().getMongo().getClientServers();
    }

    public void registerHandler(ClientEventHandler eventHandler) {
        events.add(eventHandler);
    }

    public Boolean runWorker(long start) {
        for (Document doc : clientServers.find(new Document("destination", BackupCore.getInstance().getNodeID()).append("loaded", false))) {

            if(!recentIDs.contains(doc.getObjectId("_id").toHexString())) {
                recentIDs.add(doc.getObjectId("_id").toHexString());

                doc.replace("loaded", true);
                if (doc.getString("id").endsWith("-pending")) {
                    BackupCore.getInstance().getMongoManager().getClientWorker().removeDocument(doc.getObjectId("_id"));
                } else {
                    BackupCore.getInstance().getMongoManager().getClientWorker().replaceDocument(new Document("_id", doc.getObjectId("_id")), doc);
                }
                new Thread(() -> {
                    for (ClientEventHandler eventHandler : events) {
                        eventHandler.ClientEventHandler(new MongoDocument(new Document(doc)));
                    }
                }).start();
            }
        }
        return true;
    }


    public void loadManager(boolean loadAll) {
        if(loadAll) {
            for (Document doc : clientServers.find()) {
                ClientServerObject clientServerObject = new ClientServerObject(doc);
                BackupCore.getInstance().getClientServerManager().addClientServer(clientServerObject);
            }
        } else {
            for (Document doc : clientServers.find(new Document("destination", BackupCore.getInstance().getNodeID()))) {
                ClientServerObject clientServerObject = new ClientServerObject(doc);
                BackupCore.getInstance().getClientServerManager().addClientServer(clientServerObject);
            }
        }
    }

    public void removeRecentID(String id) {
        this.recentIDs.remove(id);
    }
}
