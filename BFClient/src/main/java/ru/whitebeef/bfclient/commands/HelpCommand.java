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

        int maxLen = 0;
        for(Command cmd : cm.getCommands()) {
            maxLen = Math.max(maxLen, cmd.getLabel().length());
        }

        int finalMaxLen = maxLen;
        cm.getCommands().forEach(command -> System.out.println("> " + command.getLabel() + " ".repeat(finalMaxLen - command.getLabel().length()) + " | " + command.getDescription()));
    }
}
