package net.xilla.backupcore.api.database.worker.client;

import com.mongodb.client.MongoCollection;
import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.Log;
import net.xilla.backupcore.api.database.MongoDocument;
import net.xilla.backupcore.api.database.worker.response.ResponseEventHandler;
import net.xilla.backupcore.api.worker.Worker;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.*;

public class ClientWorker extends Worker {

    private List<Document> addQueue;
    private List<ObjectId> removeQueue;
    private HashMap<Document, Document> replaceQueue;
    private MongoCollection<Document> clientServers;

    public ClientWorker() {
        super(50);
        this.addQueue = new ArrayList<>();
        this.removeQueue = new ArrayList<>();
        this.replaceQueue = new HashMap<>();
        this.clientServers = BackupCore.getInstance().getMongo().getClientServers();
    }

    @Override
    public Boolean runWorker(long start) {
        while(removeQueue.size() > 0) {
            ObjectId id = removeQueue.remove(0);

            new Thread(() -> {
                this.clientServers.deleteOne(new Document("_id", id));

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {}

                BackupCore.getInstance().getMongoManager().getClientManager().removeRecentID(id.toHexString());
            }).start();

        }
        while(replaceQueue.size() > 0) {
            Document oldDocument = replaceQueue.keySet().iterator().next();
            Document newDocument = replaceQueue.remove(oldDocument);
            ObjectId id = oldDocument.getObjectId("_id");

            this.clientServers.replaceOne(oldDocument, newDocument);
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {}

                BackupCore.getInstance().getMongoManager().getClientManager().removeRecentID(id.toHexString());

            }).start();
        }
        if(addQueue.size() > 0) {
            ArrayList<Document> temp = new ArrayList<>(addQueue);
            addQueue = new ArrayList<>();
            this.clientServers.insertMany(temp);
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
