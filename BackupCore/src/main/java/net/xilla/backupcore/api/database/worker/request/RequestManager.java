package net.xilla.backupcore.api.database.worker.request;

import com.mongodb.client.MongoCollection;
import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.database.MongoDocument;
import net.xilla.backupcore.api.request.RequestObject;
import net.xilla.backupcore.api.worker.Worker;
import net.xilla.backupcore.nodesystem.NodeObject;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class RequestManager extends Worker {

    private MongoCollection<Document> requests;
    private List<RequestEventHandler> events;
    private List<String> recentIDs;

    public RequestManager() {
        super(50);
        this.events = new ArrayList<>();
        this.recentIDs = new ArrayList<>();
        this.requests = BackupCore.getInstance().getMongo().getRequests();
    }

    public void registerHandler(RequestEventHandler eventHandler) {
        events.add(eventHandler);
    }

    public Boolean runWorker(long start) {
        for (Document doc : requests.find(new Document("destination", BackupCore.getInstance().getNodeID()).append("loaded", false))) {

            if(!recentIDs.contains(doc.getObjectId("_id").toHexString())) {
                recentIDs.add(doc.getObjectId("_id").toHexString());

                BackupCore.getInstance().getMongoManager().getRequestWorker().removeDocument(doc.getObjectId("_id"));
                new Thread(() -> {
                    for (RequestEventHandler eventHandler : events) {
                        eventHandler.RequestEventHandler(new MongoDocument(new Document(doc))); // PASSES DOCUMENT
                    }
                }).start();
            }

        }
        return true;
    }

    public void loadManager() {
        for (Document doc : requests.find(new Document("destination", BackupCore.getInstance().getNodeID()))) {
            BackupCore.getInstance().getMongoManager().getRequestWorker().removeDocument(doc.getObjectId("_id"));

            for (RequestEventHandler eventHandler : events) {
                eventHandler.RequestEventHandler(new MongoDocument(doc)); // PASSES DOCUMENT
            }
        }
    }

    public void removeRecentID(String id) {
        this.recentIDs.remove(id);
    }
}
