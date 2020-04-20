package net.xilla.backupcore.api.database;

import net.xilla.backupcore.api.database.worker.client.ClientManager;
import net.xilla.backupcore.api.database.worker.client.ClientWorker;
import net.xilla.backupcore.api.database.worker.node.NodeManager;
import net.xilla.backupcore.api.database.worker.node.NodeWorker;
import net.xilla.backupcore.api.database.worker.request.RequestDBManager;
import net.xilla.backupcore.api.database.worker.request.RequestDBWorker;
import net.xilla.backupcore.api.database.worker.response.ResponseDBManager;
import net.xilla.backupcore.api.database.worker.response.ResponseDBWorker;
import net.xilla.backupcore.api.database.worker.storage.StorageManager;
import net.xilla.backupcore.api.database.worker.storage.StorageDBWorker;

public class MongoManager {

    private ClientWorker clientWorker;
    private NodeWorker nodeWorker;
    private RequestDBWorker requestWorker;
    private ResponseDBWorker responseWorker;
    private StorageDBWorker storageWorker;

    private ClientManager clientManager;
    private NodeManager nodeManager;
    private RequestDBManager requestManager;
    private ResponseDBManager responseManager;
    private StorageManager storageManager;

    public MongoManager() {
        this.clientWorker = new ClientWorker();
        this.nodeWorker = new NodeWorker();
        this.requestWorker = new RequestDBWorker();
        this.responseWorker = new ResponseDBWorker();
        this.storageWorker = new StorageDBWorker();

        this.clientManager = new ClientManager();
        this.nodeManager = new NodeManager();
        this.requestManager = new RequestDBManager();
        this.responseManager = new ResponseDBManager();
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

    public ResponseDBManager getResponseManager() {
        return responseManager;
    }

    public RequestDBManager getRequestManager() {
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

    public RequestDBWorker getRequestWorker() {
        return requestWorker;
    }

    public ResponseDBWorker getResponseWorker() {
        return responseWorker;
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public StorageDBWorker getStorageWorker() {
        return storageWorker;
    }
}
