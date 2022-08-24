package ru.whitebeef.bfclient.managers;

public abstract class AbstractCommandManager implements CommandManager {
    private static CommandManager instance;

    public static void setInstance(CommandManager instance) {
        AbstractCommandManager.instance = instance;
    }

    public static CommandManager getInstance() {
        return instance;
    }
}
