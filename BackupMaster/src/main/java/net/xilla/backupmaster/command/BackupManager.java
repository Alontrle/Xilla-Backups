package net.xilla.backupmaster.command;

import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.Data;
import net.xilla.backupcore.api.Log;
import net.xilla.backupcore.api.database.MongoDocument;
import net.xilla.backupcore.api.database.worker.client.ClientManager;
import net.xilla.backupcore.api.manager.ManagerCache;
import net.xilla.backupcore.api.manager.ManagerObject;
import net.xilla.backupcore.commandsystem.CommandObject;
import net.xilla.backupcore.filesystem.filemap.FileMapObject;
import net.xilla.backupcore.nodesystem.NodeObject;
import net.xilla.backupcore.nodesystem.clientserver.ClientServerObject;
import net.xilla.backupmaster.manager.backup.BackupWorker;
import net.xilla.backupmaster.manager.backup.response.RestoreFile;
import org.bson.Document;

import javax.tools.FileObject;
import java.util.Date;

public class BackupManager extends CommandObject {

    public BackupManager() {
        super("backups", new String[] {"bm", "backupmanager", "backups"}, "Manage your backups");
    }

    public boolean run(String[] args) {

        if(args.length > 0) {
            NodeObject nodeObject = BackupCore.getInstance().getNodeManager().getNodeByID(args[0]);
            if(nodeObject == null) {
                nodeObject = BackupCore.getInstance().getNodeManager().getNodeByIdentifier(args[0]);
            }
            if(nodeObject != null) {
                if(args.length > 2) {
                    if(args[1].equalsIgnoreCase("list")) {
                        if(args.length == 3 && args[2].equalsIgnoreCase("clients")) {
                            Log.sendMessage(0, new Data().getLineBreak());
                            Log.sendMessage(0, "Available Client Servers");
                            Log.sendMessage(0, new Data().getLineBreak());
                            for(String id : nodeObject.getClientServerIDs()) {
                                Log.sendMessage(0, " > " + id);
                            }
                            Log.sendMessage(0, new Data().getLineBreak());
                            return true;
                        } else if(args.length == 4) {
                            ClientServerObject client = BackupCore.getInstance().getClientServerManager().getServerByID(args[2]);
                            if(client != null) {
                                Log.sendMessage(0, new Data().getLineBreak());
                                Log.sendMessage(0, "Available Backups");
                                Log.sendMessage(0, new Data().getLineBreak());
                                for(long time : client.getBackups().keySet()) {
                                    Log.sendMessage(0, " > " + new Date(time).toString() + " (" + client.getBackups().get(time).getBackupTime() + ")");
                                }
                                Log.sendMessage(0, new Data().getLineBreak());
                            } else {
                                Log.sendMessage(2, "That is not a valid server id!");
                            }
                            return true;
                        }
                    }
                    else if(args[1].equalsIgnoreCase("backup")) {
                        if(args.length > 3) {
                            Log.sendMessage(0, "0a " + args[2]);
                            for(ManagerObject managerObject : BackupCore.getInstance().getClientServerManager().getList()) {
                                ClientServerObject client = (ClientServerObject)managerObject;
                                Log.sendMessage(0, client.toDocument().toString());
                            }
                            Log.sendMessage(0, "0b " + args[2]);
                            ClientServerObject client = BackupCore.getInstance().getClientServerManager().getServerByID(args[2]);
                            Log.sendMessage(0, "1");
                            if(client != null) {
                                Log.sendMessage(0, "2");
                                try {
                                    Log.sendMessage(0, "3");
                                    FileMapObject fileMapObject = client.getBackupByTime(Long.parseLong(args[3]));
                                    Log.sendMessage(0, "4");
                                    for(Document document : fileMapObject.getFiles()) {
                                        Log.sendMessage(0, "5");
                                        MongoDocument mongoDocument = new MongoDocument(new RestoreFile(nodeObject.getKey(), document).toDocument().append("loaded", false));
                                        Log.sendMessage(0, "6");
                                        BackupCore.getInstance().getMongoManager().getResponseWorker().addDocument(mongoDocument);
                                    }
                                    Log.sendMessage(0, "Queuing file restoration!");

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    Log.sendMessage(2, "Invalid input. Please try \"bm " + nodeObject.getIdentifier() + " backup <client id> <backup id>");
                                }
                            } else
                                Log.sendMessage(2, "That is not a valid client. Please try \"bm " + nodeObject.getIdentifier() + " backup <client id> <backup id>");
                        } else
                            Log.sendMessage(2, "Please try \"bm " + nodeObject.getIdentifier() + " backup <client id> <backup id>");
                        return true;
                    }
                }
                Log.sendMessage(0, new Data().getLineBreak());
                Log.sendMessage(0, "Available Options");
                Log.sendMessage(0, new Data().getLineBreak());
                Log.sendMessage(0, " > bm " + args[0] + " list clients - List available client servers");
                Log.sendMessage(0, " > bm " + args[0] + " list <client> backups - List available backups");
                Log.sendMessage(0, " > bm " + args[0] + " backup <client id> <backup id> - Restore a backup");
                Log.sendMessage(0, new Data().getLineBreak());
            } else
                Log.sendMessage(2, "That is not a valid client server!");
        } else {
            Log.sendMessage(0, new Data().getLineBreak());
            Log.sendMessage(0, "Available Nodes");
            Log.sendMessage(0, new Data().getLineBreak());

            for (ManagerObject managerObject : BackupCore.getInstance().getNodeManager().getList()) {
                NodeObject nodeObject = (NodeObject) managerObject;
                Log.sendMessage(0, nodeObject.getIdentifier() + " with " + nodeObject.getClientServerIDs().size() + " client servers (" + nodeObject.getKey() + ")");
            }

            Log.sendMessage(0, " > bm <identifier>");
            Log.sendMessage(0, new Data().getLineBreak());
        }
        return true;
    }

}
