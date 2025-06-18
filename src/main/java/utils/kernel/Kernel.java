package utils.kernel;


import client.Client;
import models.User;
import transfer.Request;
import transfer.Response;
import commands.*;
import commands.exceptions.WrongCommandFoundException;
import managers.CommandManager;
import managers.ProductManager;
import models.Product;
import utils.console.ConsoleHandler;
import utils.files.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class Kernel {
    private boolean exitProgram = false;
    public ConsoleHandler consoleManager = new ConsoleHandler();
    private final CommandManager commandManager = new CommandManager();
    private Request request = new Request();

    private Client client;
    private User user;
    private Response response = new Response();

    public void setCommands() {
        this.commandManager.addCommand(new Add(this.request, this.consoleManager));
        this.commandManager.addCommand(new Exit(this));
        this.commandManager.addCommand(new ExecuteScript(this));
    }
    public void exitProgram() {
        this.exitProgram = true;
    }

    public Kernel(User user) {
        this.user = user;
        // TODO 192.168.10.80 - helios lcoal ip
        request.setUser(this.user);
        this.client = new Client("localhost", 2205, false);
    }
    public void runProgram() {
        try {
            client.open();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.consoleManager.printString("-> ");
        while (!this.exitProgram && this.consoleManager.getInputStream().hasNextLine()) {
            String currentInput = this.consoleManager.getInputString().trim();
            if (this.exitProgram || currentInput.isEmpty())
                break;

            try {
                // Выполняем команду локально
                if (this.commandManager.getCommandsList().containsKey(currentInput.split(" ")[0])) {
                    this.executeCommand(currentInput);
                }
                request.setCommand(currentInput);
                client.sendRequest(request);
                this.response = (Response) client.receiveResponse(5000);
                this.request.setProduct(null);
                this.request.setCommand(null);
                if (response != null) {
                    while (response.getCommand() != null) {
                        if (this.commandManager.getCommandsList().containsKey(response.getCommand())) {
                            this.executeCommand(response.getCommand());
                        }
                        client.sendRequest(request);
                        response = (Response) client.receiveResponse(5000);
                        this.request.setProduct(null);
                        this.request.setCommand(null);
                        if (response.getCommand() == null) break;
                    }
                    if (response.getResponse() != null) {
                        System.out.println(response.getResponse());
                    }
                }

                this.consoleManager.printString("-> ");
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void executeCommand(String currentInput) {
        String[] currentArguments = Arrays.stream(currentInput.replaceAll("\\s+", " ").trim().split(" "))
                .skip(1).toArray(String[]::new);
        Executable currentCommand = this.commandManager.getCommandsList().get(currentInput.split(" ")[0]);
        try {
            if (null == currentCommand) {
                throw new WrongCommandFoundException();
            } else {
                if (currentCommand.getNeededArguments() || currentArguments.length > 0) {
                    currentCommand.execute(currentArguments);
                    return;
                }
                currentCommand.execute();

            }

        } catch (Exception exception) {
            this.consoleManager.printStringln(exception.getMessage());
        }
    }
}
