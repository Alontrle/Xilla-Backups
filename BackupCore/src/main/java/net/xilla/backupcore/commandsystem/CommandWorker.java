package net.xilla.backupcore.commandsystem;

import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.Data;
import net.xilla.backupcore.api.Log;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class CommandWorker extends Thread {

    private boolean isActive = true;

    @Override
    public void run() {
        Log.sendMessage(0, new Data().getLineBreak());
        Log.sendMessage(0, new Data().getVersion());
        Log.sendMessage(0, new Data().getLineBreak());

        CommandManager commandManager = BackupCore.getInstance().getCommandManager();
        Log.sendMessage(0, "Type \"?\" to view possible commands");
        while(true) {
            if(!isActive)
                break;

            Scanner scanner = new Scanner(System.in);
            try {
                String command = scanner.nextLine();
                if(command != null)
                    commandManager.runCommand(command);
            } catch (NoSuchElementException ignore) {}
        }
        System.exit(0);
    }

    @Override
    public void interrupt() {
        Log.sendMessage(0, "Attempting soft shutdown.");
        isActive = false;
        try {
            Thread.sleep(10000);
            Log.sendMessage(1, "Frozen for 10 seconds, forcing shutdown.");
            super.interrupt();
            System.exit(0);

        } catch (InterruptedException ignore) {}
    }

}
