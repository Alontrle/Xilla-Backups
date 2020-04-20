package net.xilla.backupcore.api.response;

import net.xilla.backupcore.api.manager.ManagerParent;

public class ResponseManager extends ManagerParent {

    public void addResponse(ResponseObject responseObject) {
        addObject(responseObject);
    }

    public void removeResponse(ResponseObject responseObject) {
        addObject(responseObject);
    }
}
