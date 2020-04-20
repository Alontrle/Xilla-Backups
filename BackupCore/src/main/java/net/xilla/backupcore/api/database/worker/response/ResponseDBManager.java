package net.xilla.backupcore.api.database.worker.response;

import com.mongodb.client.MongoCollection;
import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.database.MongoDocument;
import net.xilla.backupcore.api.database.worker.request.RequestEventHandler;
import net.xilla.backupcore.api.worker.Worker;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ResponseDBManager extends Worker {

    private MongoCollection<Document> responses;
    private List<ResponseEventHandler> events;
    private List<String> recentIDs;

    public ResponseDBManager() {
        super(50);
        this.events = new Vector<>();
        this.recentIDs = new Vector<>();
        this.responses = BackupCore.getInstance().getMongo().getResponses();
    }

    public void registerHandler(ResponseEventHandler eventHandler) {
        events.add(eventHandler);
    }

    public Boolean runWorker(long start) {
        for (Document doc : responses.find(new Document("destination", BackupCore.getInstance().getNodeID()).append("loaded", false))) {
            System.out.println("response - " + doc);
            new Thread(() -> {
                if(!recentIDs.contains(doc.getObjectId("_id").toHexString())) {
                    recentIDs.add(doc.getObjectId("_id").toHexString());

                    BackupCore.getInstance().getMongoManager().getResponseWorker().removeDocument(doc.getObjectId("_id"));

                    for (ResponseEventHandler eventHandler : events) {
                        eventHandler.ResponseEventHandler(new MongoDocument(new Document(doc))); // PASSES DOCUMENT
                    }
                }
            }).start();
        }
        return true;
    }

    public void loadManager() {
        for (Document doc : responses.find(new Document("destination", BackupCore.getInstance().getNodeID()))) {
            BackupCore.getInstance().getMongoManager().getResponseWorker().removeDocument(doc.getObjectId("_id"));

            for (ResponseEventHandler eventHandler : events) {
                eventHandler.ResponseEventHandler(new MongoDocument(doc)); // PASSES DOCUMENT
            }
        }
    }

    public void removeRecentID(String id) {
        this.recentIDs.remove(id);
    }
}
