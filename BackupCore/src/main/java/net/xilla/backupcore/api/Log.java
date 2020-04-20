package net.xilla.backupcore.api;

import net.xilla.backupcore.api.config.ConfigManager;

public class Log {

    private static boolean verboseMessages = true;

    public Log() {
        verboseMessages = ConfigManager.getInstance().getConfig("settings.json").getBoolean("verbose");
    }

    public static void sendMessage(int status, String msg) {
        if(status == 0)
            System.out.println("[INFO] " + msg);
        else if(status == 1)
            System.out.println("[WARN] " + msg);
        else if(status == 2)
            System.out.println("[ERROR] " + msg);
        else if(status == 3)
            if(verboseMessages)
                System.out.println("[VERBOSE] " + msg);
    }

    public static void toggleVerbose(boolean toggle) {
        verboseMessages = toggle;
    }

}
