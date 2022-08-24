package ru.whitebeef.bfclient.managers;

import ru.whitebeef.bfclient.commands.Command;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

public class SimpleCommandManager extends AbstractCommandManager {

    private final TreeMap<String, Command> commands = new TreeMap<>();

    public SimpleCommandManager() {
        AbstractCommandManager.setInstance(this);
    }

    @Override
    public Set<Command> getCommands() {
        return new HashSet<>(commands.values());
    }

    @Override
    public void registerCommand(Command command) {
        if(commands.containsKey(command.getLabel()))
            throw new IllegalStateException("Command " + command.getLabel() + " already registered in " + commands.get(command.getLabel()).getClass() + " ");
        commands.put(command.getLabel(), command);
    }

    @Override
    public void unregisterCommand(String label) {
        commands.remove(label);
    }

    @Override
    public Command getCommand(String label) {
        return commands.get(label);
    }

    @Override
    public boolean hasCommand(String label) {
        return commands.containsKey(label);
    }


}
