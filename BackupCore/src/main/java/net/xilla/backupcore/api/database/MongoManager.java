package net.xilla.backupcore.api.database;

import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.database.worker.client.ClientManager;
import net.xilla.backupcore.api.database.worker.client.ClientWorker;
import net.xilla.backupcore.api.database.worker.node.NodeManager;
import net.xilla.backupcore.api.database.worker.node.NodeWorker;
import net.xilla.backupcore.api.database.worker.request.RequestManager;
import net.xilla.backupcore.api.database.worker.request.RequestWorker;
import net.xilla.backupcore.api.database.worker.response.ResponseManager;
import net.xilla.backupcore.api.database.worker.response.ResponseWorker;
import net.xilla.backupcore.api.database.worker.storage.StorageManager;
import net.xilla.backupcore.api.database.worker.storage.StorageWorker;
import net.xilla.backupcore.api.worker.Worker;
import net.xilla.backupcore.filesystem.filemap.FileMapManager;

public class MongoManager {

    private ClientWorker clientWorker;
    private NodeWorker nodeWorker;
    private RequestWorker requestWorker;
    private ResponseWorker responseWorker;
    private StorageWorker storageWorker;

    private ClientManager clientManager;
    private NodeManager nodeManager;
    private RequestManager requestManager;
    private ResponseManager responseManager;
    private StorageManager storageManager;

    public MongoManager() {
        this.clientWorker = new ClientWorker();
        this.nodeWorker = new NodeWorker();
        this.requestWorker = new RequestWorker();
        this.responseWorker = new ResponseWorker();
        this.storageWorker = new StorageWorker();

        this.clientManager = new ClientManager();
        this.nodeManager = new NodeManager();
        this.requestManager = new RequestManager();
        this.responseManager = new ResponseManager();
        this.storageManager = new StorageManager();
    }

    public void start() {
        clientWorker.start();
        nodeWorker.start();
        requestWorker.start();
        responseWorker.start();
        storageWorker.start();

        clientManager.start();
        requestManager.start();
        responseManager.start();

        nodeManager.loadManager();
        requestManager.loadManager();
        responseManager.loadManager();
        storageManager.loadManager();
    }

    public ResponseManager getResponseManager() {
        return responseManager;
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }

    public ClientManager getClientManager() {
        return clientManager;
    }

    public NodeManager getNodeManager() {
        return nodeManager;
    }

    public ClientWorker getClientWorker() {
        return clientWorker;
    }

    public NodeWorker getNodeWorker() {
        return nodeWorker;
    }

    public RequestWorker getRequestWorker() {
        return requestWorker;
    }

    public ResponseWorker getResponseWorker() {
        return responseWorker;
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public StorageWorker getStorageWorker() {
        return storageWorker;
    }
}
