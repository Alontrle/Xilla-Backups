package net.xilla.backupslave.manager.node.response;

import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.config.Config;
import net.xilla.backupcore.api.config.ConfigManager;
import net.xilla.backupcore.api.database.MongoDocument;
import net.xilla.backupcore.api.database.worker.response.ResponseEventHandler;
import org.bson.Document;

public class NodeAssignment extends ResponseEventHandler {

    @Override
    public void ResponseEventHandler(MongoDocument document) {
        Document doc = document.getDocument();
        if(doc.getString("type").equalsIgnoreCase("NodeAssignment")) {
            Config config = ConfigManager.getInstance().getConfig("settings.json");
            config.set("node-id", doc.getString("data"));
            config.save();
        }
    }

}
