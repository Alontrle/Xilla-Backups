package net.xilla.backupcore.api.manager;

import java.util.HashMap;

public class ManagerCache {

    private HashMap<String, Object> cache;

    public ManagerCache() {
        cache = new HashMap<>();
    }

    public void putObject(String key, Object object) {
        cache.put(key, object);
    }

    public boolean isCached(String key) {
        return cache.containsKey(key);
    }

    public Object getObject(String key) {
        return cache.get(key);
    }

    public void removeObject(String key) {
        cache.remove(key);
    }
}
