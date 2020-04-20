package net.xilla.backupcore.api.database.worker.node;

import com.mongodb.client.MongoCollection;
import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.Log;
import net.xilla.backupcore.api.database.MongoDocument;
import net.xilla.backupcore.api.worker.Worker;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;

public class NodeWorker extends Worker {

    private List<Document> addQueue;
    private List<ObjectId> removeQueue;
    private HashMap<Document, Document> replaceQueue;
    private MongoCollection<Document> nodes;

    public NodeWorker() {
        super(50);
        this.addQueue = new ArrayList<>();
        this.removeQueue = new ArrayList<>();
        this.replaceQueue = new HashMap<>();
        this.nodes = BackupCore.getInstance().getMongo().getNodes();
    }

    @Override
    public Boolean runWorker(long start) {
        while(removeQueue.size() > 0) {
            ObjectId id = removeQueue.remove(0);
            new Thread(() -> {
                this.nodes.deleteOne(new Document("_id", id));

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {
                }

            }).start();
        }

        while(replaceQueue.size() > 0) {
            Document oldDocument = replaceQueue.keySet().iterator().next();
            Document newDocument = replaceQueue.remove(oldDocument);

            this.nodes.replaceOne(oldDocument, newDocument);
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {
                }

                ObjectId id = oldDocument.getObjectId("_id");

            }).start();

        }

        if(addQueue.size() > 0) {
            ArrayList<Document> temp = new ArrayList<>(addQueue);
            addQueue = new ArrayList<>();
            this.nodes.insertMany(temp);
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
