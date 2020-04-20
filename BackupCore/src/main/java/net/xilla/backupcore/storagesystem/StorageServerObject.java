package net.xilla.backupcore.storagesystem;

import net.xilla.backupcore.api.manager.ManagerObject;
import net.xilla.backupcore.api.worker.ObjectWorker;
import net.xilla.backupcore.filesystem.file.FileObject;
import org.bson.Document;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class StorageServerObject extends ObjectWorker {

    private String id;
    private String host;
    private int port;
    private String user;
    private String password;
    private List<String> usedIDs;
    private String type;

    public StorageServerObject(String id, String host, int port, String user, String password, List<String> usedIDs, String type) {
        super(50);
        this.id = id;
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.usedIDs = usedIDs;
        this.type = type;
    }

    public StorageServerObject(Document document) {
        super(10);
        this.id = document.getString("id");
        this.host = document.getString("host");
        this.user = document.getString("user");
        this.port = document.getInteger("port");
        this.password = document.getString("password");
        this.usedIDs = document.getList("usedIDs", String.class);
        this.type = document.getString("type");
    }

    public String getUser() {
        return user;
    }

    public String getHost() {
        return host;
    }

    public String getPassword() {
        return password;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public Document toDocument() {
        return new Document()
                .append("id", id)
                .append("port", port)
                .append("host", host)
                .append("user", user)
                .append("password", password)
                .append("usedIDs", usedIDs)
                .append("type", type);
    }

    public String getUnusedID() {
        String uuid = UUID.randomUUID().toString();
        while(usedIDs.contains(uuid)) {
            uuid = UUID.randomUUID().toString();
        }
        usedIDs.add(uuid);
        return uuid;
    }

    public int getPort() {
        return port;
    }

    public String getID() {
        return id;
    }

    public void removeID(String id) {
        usedIDs.remove(id);
    }

    public boolean uploadFile(FileObject file) {
        return false; // OVERRIDE
    }

    public boolean downloadFile(FileObject file) {
        return false; // OVERRIDE
    }

    public boolean deleteFile(FileObject file) {
        return false; // OVERRIDE
    }

    public boolean open() {
        return false; // OVERRIDE
    }

    public boolean close() {
        return false; // OVERRIDE
    }
}
