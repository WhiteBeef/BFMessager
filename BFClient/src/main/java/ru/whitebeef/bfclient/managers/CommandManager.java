package ru.whitebeef.bfclient.managers;

import ru.whitebeef.bfclient.commands.Command;

import java.util.Set;

public interface CommandManager {

    Set<Command> getCommands();

    void registerCommand(Command command);

    void unregisterCommand(String label);

    Command getCommand(String label);

    boolean hasCommand(String label);

}
