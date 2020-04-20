package net.xilla.backupcore.api.database.worker.storage;

import com.mongodb.client.MongoCollection;
import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.Log;
import net.xilla.backupcore.api.database.MongoDocument;
import net.xilla.backupcore.api.worker.Worker;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;

public class StorageWorker extends Worker {

    private List<Document> addQueue;
    private List<ObjectId> removeQueue;
    private HashMap<Document, Document> replaceQueue;
    private MongoCollection<Document> storageServers;

    public StorageWorker() {
        super(50);
        this.addQueue = new ArrayList<>();
        this.removeQueue = new ArrayList<>();
        this.replaceQueue = new HashMap<>();
        this.storageServers = BackupCore.getInstance().getMongo().getStorageServers();
    }

    @Override
    public Boolean runWorker(long start) {
        while(removeQueue.size() > 0) {
            this.storageServers.deleteOne(new Document("_id", removeQueue.remove(0)));
        }
        while(replaceQueue.size() > 0) {
            Document oldDocument = replaceQueue.keySet().iterator().next();
            Document newDocument = replaceQueue.remove(oldDocument);
            this.storageServers.replaceOne(oldDocument, newDocument);
        }
        if(addQueue.size() > 0) {
            ArrayList<Document> temp = new ArrayList<>(addQueue);
            addQueue = new ArrayList<>();
            this.storageServers.insertMany(temp);
        }
        return true;
    }

    public void addDocument(Document document) {
        addQueue.add(document);
    }

    public void replaceDocument(Document oldDocument, Document newDocument) {
        replaceQueue.put(oldDocument, newDocument);
    }

    public void removeDocument(ObjectId id) {
        removeQueue.add(id);
    }

}
