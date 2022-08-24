package ru.whitebeef.bfclient.commands;

import ru.whitebeef.bfclient.BFClient;

public class ExitCommand implements Command {
    @Override
    public String getLabel() {
        return "exit";
    }

    @Override
    public String getDescription() {
        return "exit program";
    }

    @Override
    public void execute(String[] args) {
        BFClient.getInstance().interrupt();
    }
}
