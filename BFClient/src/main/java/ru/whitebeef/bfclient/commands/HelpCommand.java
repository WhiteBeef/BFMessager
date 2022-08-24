package ru.whitebeef.bfclient.commands;

import ru.whitebeef.bfclient.managers.AbstractCommandManager;
import ru.whitebeef.bfclient.managers.CommandManager;

public class HelpCommand implements Command {

    @Override
    public String getLabel() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "show all available commands";
    }

    @Override
    public void execute(String[] args) {
        System.out.println("Help for you:");

        CommandManager cm = AbstractCommandManager.getInstance();

        cm.getCommands().forEach(command -> System.out.println("> " + command.getLabel() + " | " + command.getDescription()));

    }
}
