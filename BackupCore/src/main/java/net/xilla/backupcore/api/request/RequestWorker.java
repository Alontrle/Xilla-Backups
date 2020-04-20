package net.xilla.backupcore.api.request;

import net.xilla.backupcore.api.worker.Worker;

public class RequestWorker extends Worker {

    public RequestWorker() {
        super(500);
    }

}
