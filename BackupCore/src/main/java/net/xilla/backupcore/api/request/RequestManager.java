package net.xilla.backupcore.api.request;

import net.xilla.backupcore.api.manager.ManagerParent;

public class RequestManager extends ManagerParent {

    public void addRequest(RequestObject requestObject) {
        addObject(requestObject);
    }

    public void removeRequest(RequestObject requestObject) {
        addObject(requestObject);
    }
}
