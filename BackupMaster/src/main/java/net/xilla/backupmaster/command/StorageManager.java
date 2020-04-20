package net.xilla.backupmaster.command;

import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.Data;
import net.xilla.backupcore.api.Log;
import net.xilla.backupcore.commandsystem.CommandObject;
import net.xilla.backupcore.storagesystem.StorageServerObject;
import net.xilla.backupcore.storagesystem.type.FTPServer;
import net.xilla.backupcore.storagesystem.type.SFTPServer;

import java.util.ArrayList;
import java.util.UUID;

public class StorageManager extends CommandObject {

    public StorageManager() {
        super("storage", new String[] {"sm", "storagemanager", "storage"}, "Manage your storage");
    }

    public boolean run(String[] args) {
        if(args.length > 0) {
            if(args[0].equalsIgnoreCase("add")) {
                if(args.length == 6) {
                    String type = args[1];
                    String host = args[2];
                    int port = Integer.parseInt(args[3]);
                    String user = args[4];
                    String pass = args[5];

                    StorageServerObject storageServerObject = null;
                    if(type.equalsIgnoreCase("SFTP")) {
                        storageServerObject = new SFTPServer(UUID.randomUUID().toString(), host, port, user, pass, new ArrayList<>());
                    } else if(type.equalsIgnoreCase("FTP")) {
                        storageServerObject = new FTPServer(UUID.randomUUID().toString(), host, port, user, pass, new ArrayList<>());
                    }

                    if(storageServerObject != null) {
                        BackupCore.getInstance().getStorageServerManager().registerServer(storageServerObject);
                        BackupCore.getInstance().getMongoManager().getStorageWorker().addDocument(storageServerObject.toDocument());
                        Log.sendMessage(0, "Successfully added the storage server!");
                        return true;
                    }

                    Log.sendMessage(2, "Failed to create the storage server. Please try with \"sm add <FTP/SFTP> <host> <port> <user> <password>\"");
                    return true;
                }
            } else if(args[0].equalsIgnoreCase("info")) {
                if(args.length == 2) {
                    StorageServerObject storageServerObject = BackupCore.getInstance().getStorageServerManager().getStorageServerByID(args[1]);
                    if(storageServerObject != null) {
                        Log.sendMessage(0, new Data().getLineBreak());
                        Log.sendMessage(0, "Storage Server Information");
                        Log.sendMessage(0, new Data().getLineBreak());
                        Log.sendMessage(0, " > Host: " + storageServerObject.getHost());
                        Log.sendMessage(0, " > Port: " + storageServerObject.getPort());
                        Log.sendMessage(0, " > User: " + storageServerObject.getUser());
                        Log.sendMessage(0, " > Pass: " + storageServerObject.getPassword());
                        Log.sendMessage(0, " > Type: " + storageServerObject.getType());
                        Log.sendMessage(0, new Data().getLineBreak());
                    } else
                        Log.sendMessage(2, "Invalid storage server!");
                    return true;
                }
            } else if(args[0].equalsIgnoreCase("list")) {

                Log.sendMessage(0, new Data().getLineBreak());
                Log.sendMessage(0, "Available Storage Servers");
                Log.sendMessage(0, new Data().getLineBreak());

                for (StorageServerObject storageServer : BackupCore.getInstance().getStorageServerManager().getServerObjectList()) {
                    Log.sendMessage(0, "[" + storageServer.getType().toUpperCase() + "] Host - \"" + storageServer.getHost() + ":" + storageServer.getPort() + "\", User - \"" + storageServer.getUser() + "\", ID - \"" + storageServer.getID() + "\"");
                }

                Log.sendMessage(0, " > sm info <storage id>");
                Log.sendMessage(0, new Data().getLineBreak());
                return true;
            }
        }
        Log.sendMessage(0, new Data().getLineBreak());
        Log.sendMessage(0, "Available Commands");
        Log.sendMessage(0, new Data().getLineBreak());
        Log.sendMessage(0, " > sm list");
        Log.sendMessage(0, " > sm info <storage id>");
        Log.sendMessage(0, " > sm add <FTP/SFTP> <host> <port> <user> <password>");
        Log.sendMessage(0, new Data().getLineBreak());
        return true;
    }
}
