package net.xilla.backupcore.commandsystem;

import net.xilla.backupcore.api.manager.ManagerObject;

public class CommandObject extends ManagerObject {

    private String[] activators;
    private String description;

    public CommandObject(String name, String[] activators, String description) {
        super(name, null);
        this.activators = activators;
        this.description = description;
    }

    public String getName() {
        return getKey();
    }

    public String[] getActivators() {
        return activators;
    }

    public void setActivators(String[] activators) {
        this.activators = activators;
    }

    public String getDescription() {
        return description;
    }

    public boolean run(String[] args) {
        return false; // OVERRIDE
    }
}
