package net.xilla.backupcore.api.database.worker.node;

import com.mongodb.client.MongoCollection;
import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.database.MongoDocument;
import net.xilla.backupcore.api.worker.Worker;
import net.xilla.backupcore.filesystem.file.FileObject;
import net.xilla.backupcore.nodesystem.NodeObject;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class NodeManager {

    private MongoCollection<Document> nodes;

    public NodeManager() {
        this.nodes = BackupCore.getInstance().getMongo().getNodes();
    }
    public void loadManager() {
        for (Document doc : nodes.find(new Document("destination", BackupCore.getInstance().getNodeID()))) {
            NodeObject nodeObject = new NodeObject(doc);
            BackupCore.getInstance().getNodeManager().addNode(nodeObject);
        }
    }

}
