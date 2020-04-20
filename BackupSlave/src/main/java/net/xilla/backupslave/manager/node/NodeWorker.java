package net.xilla.backupslave.manager.node;

import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.Log;
import net.xilla.backupcore.api.database.MongoDocument;
import net.xilla.backupcore.api.worker.Worker;
import net.xilla.backupslave.manager.node.request.NodePing;
import org.bson.Document;

public class NodeWorker extends Worker {

    private long lastPingBack;
    private long pings;

    public NodeWorker() {
        super(10000);
        this.lastPingBack = 0;
        this.pings = 0;
    }

    public Boolean runWorker(long start) {
        if(checkConnection()) {
            ping();
        } else {
            Log.sendMessage(2, "Failed to run the Node Worker. The master server is not responding to pings!");
            if((System.currentTimeMillis() - lastPingBack) >= 60000) {
                ping();
            }
        }
        return true;
    }

    private void ping() {
        MongoDocument mongoDocument = new MongoDocument(new NodePing().toDocument(), BackupCore.getInstance().getNodeID(), "Master");
        BackupCore.getInstance().getMongoManager().getRequestWorker().addDocument(mongoDocument);
        pings++;
    }

    private boolean checkConnection() {
        if(pings == 0)
            return true;

        return (System.currentTimeMillis() - lastPingBack) <= 30000;
    }

    public long getLastPingBack() {
        return lastPingBack;
    }

    public void setLastPingBack(long lastPingBack) {
        this.lastPingBack = lastPingBack;
    }
}
