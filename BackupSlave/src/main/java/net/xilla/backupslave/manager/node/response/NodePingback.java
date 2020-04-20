package net.xilla.backupslave.manager.node.response;

import net.xilla.backupcore.api.database.MongoDocument;
import net.xilla.backupcore.api.database.worker.response.ResponseEventHandler;
import net.xilla.backupslave.BackupSlave;
import org.bson.Document;

public class NodePingback extends ResponseEventHandler {


    @Override
    public void ResponseEventHandler(MongoDocument document) {
        Document doc = document.getDocument();
        if(doc.getString("type").equalsIgnoreCase("Pong")) {
            BackupSlave.getInstance().getNodeWorker().setLastPingBack(System.currentTimeMillis());
        }
    }

}
