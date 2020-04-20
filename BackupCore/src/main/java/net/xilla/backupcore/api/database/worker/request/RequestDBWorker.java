package net.xilla.backupcore.api.database.worker.request;

import com.mongodb.client.MongoCollection;
import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.Log;
import net.xilla.backupcore.api.database.MongoDocument;
import net.xilla.backupcore.api.worker.Worker;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;

public class RequestDBWorker extends Worker {

    private List<Document> addQueue;
    private List<ObjectId> removeQueue;
    private HashMap<Document, Document> replaceQueue;
    private MongoCollection<Document> requests;

    public RequestDBWorker() {
        super(50);
        this.addQueue = new ArrayList<>();
        this.removeQueue = new ArrayList<>();
        this.replaceQueue = new HashMap<>();
        this.requests = BackupCore.getInstance().getMongo().getRequests();
    }

    @Override
    public Boolean runWorker(long start) {
        while(removeQueue.size() > 0) {
            Log.sendMessage(0, "Running Request DB Worker " + removeQueue.size());
            ObjectId id = removeQueue.remove(0);

            this.requests.deleteOne(new Document("_id", id));
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {
                }

                BackupCore.getInstance().getMongoManager().getRequestManager().removeRecentID(id.toHexString());
            }).start();
        }
        while(replaceQueue.size() > 0) {
            Document oldDocument = replaceQueue.keySet().iterator().next();
            Document newDocument = replaceQueue.remove(oldDocument);

            this.requests.replaceOne(oldDocument, newDocument);
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {
                }

                ObjectId id = oldDocument.getObjectId("_id");
                BackupCore.getInstance().getMongoManager().getRequestManager().removeRecentID(id.toHexString());
            }).start();
        }
        if(addQueue.size() > 0) {
            ArrayList<Document> temp = new ArrayList<>(addQueue);
            addQueue = new ArrayList<>();
            this.requests.insertMany(temp);
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
