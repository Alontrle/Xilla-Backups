package net.xilla.backupmaster.manager.node.request;

import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.database.MongoDocument;
import net.xilla.backupcore.api.database.worker.request.RequestEventHandler;
import net.xilla.backupcore.nodesystem.NodeManager;
import net.xilla.backupcore.nodesystem.NodeObject;
import net.xilla.backupmaster.manager.node.response.NodeAssignment;
import org.bson.Document;

import java.util.ArrayList;

public class NodeRegister extends RequestEventHandler {

    @Override
    public void RequestEventHandler(MongoDocument document) {
        Document doc = document.getDocument();
        if(doc.getString("type").equalsIgnoreCase("NodeRegister")) {
            MongoDocument nodeAssignmentDocument = new MongoDocument(new NodeAssignment(document.getOrigin()).toDocument(), document.getDestination(), document.getOrigin());
            BackupCore.getInstance().getMongoManager().getResponseWorker().addDocument(nodeAssignmentDocument);

            NodeObject nodeObject = new NodeObject(nodeAssignmentDocument.getDocument().getString("data"), System.currentTimeMillis(), new ArrayList<>(), doc.getString("data"));
            BackupCore.getInstance().getNodeManager().addNode(nodeObject);

            MongoDocument nodeObjectDocument = new MongoDocument(nodeObject.toDocument(), "Master", "Master");
            BackupCore.getInstance().getMongoManager().getNodeWorker().addDocument(nodeObjectDocument);
        }
    }
}
