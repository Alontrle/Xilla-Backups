package net.xilla.backupcore.storagesystem;

import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.manager.ManagerCache;
import net.xilla.backupcore.api.manager.ManagerParent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StorageServerManager {

    private List<StorageServerObject> serverObjectList;
    private ManagerCache cache;

    public StorageServerManager() {
        this.serverObjectList = new ArrayList<>();
        this.cache = new ManagerCache();
    }

    public void registerServer(StorageServerObject serverObject) {
        this.serverObjectList.add(serverObject);
        cache.putObject(serverObject.getID(), serverObject);
    }

    public void unRegisterServer(StorageServerObject serverObject) {
        this.serverObjectList.remove(serverObject);
    }

    public StorageServerObject getStorageServer() {
        Random rand = new Random();
        return serverObjectList.get(rand.nextInt(serverObjectList.size()));
    }

    public StorageServerObject getStorageServerByID(String id) {
        StorageServerObject storageServerObject = (StorageServerObject)cache.getObject(id);
        if(storageServerObject == null) {
            BackupCore.getInstance().getMongoManager().getStorageManager().loadManager();
            storageServerObject = (StorageServerObject)cache.getObject(id);
        }
        return storageServerObject;
    }

    public List<StorageServerObject> getServerObjectList() {
        return serverObjectList;
    }
}
