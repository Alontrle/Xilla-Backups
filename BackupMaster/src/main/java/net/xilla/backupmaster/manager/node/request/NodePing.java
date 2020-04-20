package net.xilla.backupmaster.manager.node.request;

import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.database.MongoDocument;
import net.xilla.backupcore.api.database.worker.request.RequestEventHandler;
import net.xilla.backupmaster.manager.node.response.NodeAssignment;
import net.xilla.backupmaster.manager.node.response.NodePingback;
import org.bson.Document;

import java.util.Date;

public class NodePing extends RequestEventHandler {

    @Override
    public void RequestEventHandler(MongoDocument document) {
        Document doc = document.getDocument();
        System.out.println(new Date().toString() + "request - " + document.getDocument());
        if(doc.getString("type").equalsIgnoreCase("Ping")) {
            MongoDocument mongoDocument = new MongoDocument(new NodePingback(document.getOrigin()).toDocument(), document.getDestination(), document.getOrigin());
            BackupCore.getInstance().getMongoManager().getResponseWorker().addDocument(mongoDocument);
        }
    }

}
