package net.xilla.backupcore.api.config;

import net.xilla.backupcore.api.manager.ManagerParent;

public class ConfigManager extends ManagerParent {

    private static ConfigManager instance;

    public static ConfigManager getInstance() {
        return instance;
    }

    public ConfigManager() {
        instance = this;
    }

    public Config getConfig(String configName) {
        return (Config)getObjectWithKey(configName);
    }

    public void addConfig(Config config) {
        addObject(config);
    }

}
