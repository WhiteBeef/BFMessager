package ru.whitebeef.bfclient.commands;

public interface Command {

    String getLabel();

    String getDescription();

    void execute(String[] args) throws Exception;

}
