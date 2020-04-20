package net.xilla.backupcore.storagesystem;

import net.xilla.backupcore.api.Log;

import java.util.ArrayList;
import java.util.List;

public class StorageServerError {

    private static List<StorageServerErrorHandler> storageServerErrorHandlers = new ArrayList<>();

    public static void registerErrorHandler(StorageServerErrorHandler storageServerErrorHandler) {
        storageServerErrorHandlers.add(storageServerErrorHandler);
    }

    public void reportError(String error) {
        Log.sendMessage(2, error);
        for(StorageServerErrorHandler storageServerErrors : storageServerErrorHandlers) {
            storageServerErrors.StorageServerErrorHandler(error);
        }

    }

}
