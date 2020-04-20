package net.xilla.backupcore.nodesystem.clientserver;

import net.xilla.backupcore.api.manager.ManagerParent;

public class ClientServerManager extends ManagerParent {

    public void addClientServer(ClientServerObject clientServerObject) {
        addObject(clientServerObject);
    }

    public void removeClientServer(String clientServerID) {
        removeObject(clientServerID);
    }

    public ClientServerObject getServerByID(String id) {
        return (ClientServerObject)getCache("key").getObject(id);
    }

}
