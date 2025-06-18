package commands;

import commands.exceptions.ReccursionFoundException;
import commands.exceptions.WrongArgumentsAmountException;
import models.IncorrectStringValueException;
import utils.console.ConsoleHandler;
import utils.files.FileReader;
import utils.kernel.Kernel;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;


public class ExecuteScript extends Command {
    private Kernel kernel;



    public ExecuteScript(Kernel kernel) {
        super();
        this.isNeedArguments = true;
        this.commandArguments = 1;
        this.kernel = kernel;
    }

    @Override
    public void execute() throws WrongArgumentsAmountException {
        throw new WrongArgumentsAmountException();
    }

    @Override
    public void execute(String[] arguments)
            throws WrongArgumentsAmountException, IncorrectStringValueException {
        if (arguments.length != this.commandArguments) {
            throw new WrongArgumentsAmountException();
        }
        if (arguments[arguments.length - 1].matches("^-?\\d+$") == true) {
            throw new IncorrectStringValueException();
        }
        try {
            FileReader fileReader = new FileReader();
            List<String> commandsList = fileReader.read(arguments[arguments.length - 1]);
            if (ScriptsHandler.getScripts().contains(arguments[arguments.length - 1])) {
                throw new ReccursionFoundException();
            }
            ScriptsHandler.addScript(arguments[arguments.length - 1]);
            String input = String.join("\n", commandsList);
            InputStream scriptInput = new ByteArrayInputStream(input.getBytes());
            InputStream originalInput = System.in;
            System.setIn(scriptInput);
            this.kernel.consoleManager = new ConsoleHandler();
            this.kernel.setCommands();
            this.kernel.runProgram();
            System.setIn(originalInput);
            this.kernel.consoleManager = new ConsoleHandler();
            this.kernel.runProgram();
        } catch (FileNotFoundException exception) {
            System.out.println(exception.getMessage());
        } catch (ReccursionFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "execute_script";
    }

    @Override
    public String getDescription() {
        return "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.";
    }

}
