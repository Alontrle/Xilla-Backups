package net.xilla.backupcore;

import net.xilla.backupcore.api.config.Config;
import net.xilla.backupcore.api.config.ConfigManager;
import net.xilla.backupcore.api.database.Mongo;
import net.xilla.backupcore.api.database.MongoManager;
import net.xilla.backupcore.api.request.RequestManager;
import net.xilla.backupcore.api.response.ResponseManager;
import net.xilla.backupcore.commandsystem.CommandManager;
import net.xilla.backupcore.commandsystem.CommandWorker;
import net.xilla.backupcore.commandsystem.cmd.Clear;
import net.xilla.backupcore.commandsystem.cmd.End;
import net.xilla.backupcore.commandsystem.cmd.Help;
import net.xilla.backupcore.filesystem.file.FileManager;
import net.xilla.backupcore.filesystem.filemap.FileMapManager;
import net.xilla.backupcore.nodesystem.NodeManager;
import net.xilla.backupcore.nodesystem.clientserver.ClientServerManager;
import net.xilla.backupcore.storagesystem.StorageServerManager;

public class BackupCore {

    private static BackupCore instance;

    public static BackupCore getInstance() {
        return instance;
    }

    private String nodeID;
    private String identifier;

    private Mongo mongo;
    private MongoManager mongoManager;
    private ResponseManager responseManager;
    private RequestManager requestManager;
    private CommandManager commandManager;
    private FileManager fileManager;
    private FileMapManager fileMapManager;
    private StorageServerManager storageServerManager;
    private ClientServerManager clientServerManager;
    private NodeManager nodeManager;
    private CommandWorker commandWorker;

    public BackupCore(String nodeID, String identifier) {
        instance = this;
        this.nodeID = nodeID;
        this.identifier = identifier;

        ConfigManager configManager = new ConfigManager();
        Config config = new Config("settings.json");
        configManager.addConfig(config);

        config.loadDefault("mongodb-user", "admin");
        config.loadDefault("mongodb-pass", "password");
        config.loadDefault("mongodb-host", "localhost:27017");
        config.loadDefault("mongodb-db", "backup-database");
        config.loadDefault("mongodb-authdb", "admin");
        config.loadDefault("verbose", false);
        config.save();

        this.mongo = new Mongo();
        this.mongoManager = new MongoManager();
        this.responseManager = new ResponseManager();
        this.requestManager = new RequestManager();
        this.commandManager = new CommandManager();
        this.fileManager = new FileManager();
        this.fileMapManager = new FileMapManager();
        this.storageServerManager = new StorageServerManager();
        this.clientServerManager = new ClientServerManager();
        this.nodeManager = new NodeManager();
        this.commandWorker = new CommandWorker();

        commandManager.registerCommand(new Help());
        commandManager.registerCommand(new End());
        commandManager.registerCommand(new Clear());
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public MongoManager getMongoManager() {
        return mongoManager;
    }

    public Mongo getMongo() {
        return mongo;
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }

    public ResponseManager getResponseManager() {
        return responseManager;
    }

    public StorageServerManager getStorageServerManager() {
        return storageServerManager;
    }

    public NodeManager getNodeManager() {
        return nodeManager;
    }

    public ClientServerManager getClientServerManager() {
        return clientServerManager;
    }

    public FileMapManager getFileMapManager() {
        return fileMapManager;
    }

    public CommandWorker getCommandWorker() {
        return commandWorker;
    }

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
