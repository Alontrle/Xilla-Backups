package net.xilla.backupslave.manager.node;

import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.database.MongoDocument;
import net.xilla.backupcore.api.worker.Worker;
import net.xilla.backupslave.manager.node.request.NodePing;
import org.bson.Document;

public class NodeWorker extends Worker {

    public NodeWorker() {
        super(1000);
    }

    public Boolean runWorker(long start) {
        MongoDocument mongoDocument = new MongoDocument(new NodePing().toDocument(), BackupCore.getInstance().getNodeID(), "Master");
        BackupCore.getInstance().getMongoManager().getRequestWorker().addDocument(mongoDocument);
        return true;
    }

}
