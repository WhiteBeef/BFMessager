package ru.whitebeef.bfclient;

import ru.whitebeef.bfclient.commands.Command;
import ru.whitebeef.bfclient.commands.ConnectCommand;
import ru.whitebeef.bfclient.commands.ExitCommand;
import ru.whitebeef.bfclient.commands.HelpCommand;
import ru.whitebeef.bfclient.managers.AbstractCommandManager;
import ru.whitebeef.bfclient.managers.CommandManager;
import ru.whitebeef.bfclient.managers.SimpleCommandManager;

import java.util.Arrays;
import java.util.Scanner;

public class BFClient extends Thread {

    private static BFClient instance;

    public static BFClient getInstance() {
        return instance;
    }

    public BFClient() {
        AbstractCommandManager.setInstance(new SimpleCommandManager());

        registerCommands(new HelpCommand(), new ConnectCommand(), new ExitCommand());

        this.start();
    }


    public static void main(String[] args) {

        instance = new BFClient();

    }

    private void registerCommands(Command... commands) {
        for(Command command : commands) {
            AbstractCommandManager.getInstance().registerCommand(command);
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("Hello, client BFMessenger welcomes you! For a list of commands, type help");

            CommandManager cm = AbstractCommandManager.getInstance();

            Scanner sc = new Scanner(System.in);
            while(!this.isInterrupted()) {
                String str = sc.nextLine();
                String cmd = str.split(" ", 2)[0];
                String[] args = Arrays.stream(str.split(" ")).skip(1).toArray(String[]::new);
                if(cm.hasCommand(cmd)) {
                    try {
                        Command command = cm.getCommand(cmd);
                        command.execute(args);
                    } catch(Exception ex) {
                        System.out.println("Some troubles in command execution");
                        System.err.println(ex.getMessage() + " " + ex.getCause());
                    }
                }
                else {
                    System.out.println("'" + str + "' is unknown command! Type 'help' to see all available commands");
                }
            }
        } catch(Exception ex) {
            ex.printStackTrace();
            interrupt();
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();

        System.out.println("Bye bye!");
    }
}
