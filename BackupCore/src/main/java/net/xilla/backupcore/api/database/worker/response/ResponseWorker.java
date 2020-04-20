package net.xilla.backupcore.api.database.worker.response;

import com.mongodb.client.MongoCollection;
import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.Log;
import net.xilla.backupcore.api.database.MongoDocument;
import net.xilla.backupcore.api.worker.Worker;
import net.xilla.backupcore.nodesystem.clientserver.ClientServerObject;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;

public class ResponseWorker extends Worker {

    private List<Document> addQueue;
    private List<ObjectId> removeQueue;
    private HashMap<Document, Document> replaceQueue;
    private MongoCollection<Document> responses;

    public ResponseWorker() {
        super(50);
        this.addQueue = new ArrayList<>();
        this.removeQueue = new ArrayList<>();
        this.replaceQueue = new HashMap<>();
        this.responses = BackupCore.getInstance().getMongo().getResponses();
    }

    @Override
    public Boolean runWorker(long start) {
        while(removeQueue.size() > 0) {
            ObjectId id = removeQueue.remove(0);
            this.responses.deleteOne(new Document("_id", id));

            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {}

                BackupCore.getInstance().getMongoManager().getResponseManager().removeRecentID(id.toHexString());

            }).start();
        }
        while(replaceQueue.size() > 0) {
            Document oldDocument = replaceQueue.keySet().iterator().next();
            Document newDocument = replaceQueue.remove(oldDocument);

            this.responses.replaceOne(oldDocument, newDocument);
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {
                }

                ObjectId id = oldDocument.getObjectId("_id");
                BackupCore.getInstance().getMongoManager().getResponseManager().removeRecentID(id.toHexString());
            }).start();
        }
        if(addQueue.size() > 0) {
            ArrayList<Document> temp = new ArrayList<>(addQueue);
            addQueue = new ArrayList<>();
            this.responses.insertMany(temp);
        }
        return true;
    }

    public void addDocument(MongoDocument mongoDocument) {
        addQueue.add(mongoDocument.getFinalDocument());
    }

    public void replaceDocument(Document oldDocument, Document newDocument) {
        replaceQueue.put(oldDocument, newDocument);
    }

    public void removeDocument(ObjectId id) {
        removeQueue.add(id);
    }

}
