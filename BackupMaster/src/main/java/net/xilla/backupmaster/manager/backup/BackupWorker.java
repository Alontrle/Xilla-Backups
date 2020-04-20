package net.xilla.backupmaster.manager.backup;

import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.worker.Worker;
import net.xilla.backupcore.nodesystem.clientserver.ClientServerObject;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;

public class BackupWorker extends Worker {

    private static ArrayList<ClientServerObject> updatedClientServers = new ArrayList<>();
    private static ArrayList<ClientServerObject> newClientServers = new ArrayList<>();

    public static void updateClientServer(ClientServerObject clientServerObject) {
        updatedClientServers.add(clientServerObject);
    }

    public static void registerClientServer(ClientServerObject clientServerObject) {
        newClientServers.add(clientServerObject);
    }

    public BackupWorker() {
        super(5000);

        if(newClientServers.size() > 0) {
            ArrayList<Document> documentsToAdd = new ArrayList<>();
            for (ClientServerObject clientServer : newClientServers) {
                documentsToAdd.add(clientServer.toDocument());
            }

            BackupCore.getInstance().getMongo().getClientServers().insertMany(documentsToAdd);
        }

        if(updatedClientServers.size() > 0) {
            ArrayList<Document> documentsToUpdate = new ArrayList<>();
            for (ClientServerObject clientServer : updatedClientServers) {
                documentsToUpdate.add(clientServer.toDocument());
            }

            for (Document doc : documentsToUpdate) {
                BackupCore.getInstance().getMongo().getClientServers().replaceOne(new Document("id", doc.getString("id")), doc);
            }
        }
    }

    @Override
    public Boolean runWorker(long start) {
        setPaused(true);
        return true;
    }

}
