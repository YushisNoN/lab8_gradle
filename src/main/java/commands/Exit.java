package commands;


import commands.exceptions.WrongArgumentsAmountException;
import models.IncorrectIntegerValueException;
import models.IncorrectStringValueException;
import models.Product;
import utils.kernel.Kernel;

public class Exit extends Command{
    private Kernel kernel;

    public Exit(Kernel kernel) {
        super();
        this.isNeedAllCommands = false;
        this.isNeedCollection = false;
        this.isNeedArguments = false;
        this.kernel = kernel;
    }

    @Override
    public void execute() throws WrongArgumentsAmountException {
        this.kernel.exitProgram();
    }

    @Override
    public void execute(String[] arguments)
            throws WrongArgumentsAmountException, IncorrectStringValueException, IncorrectIntegerValueException {
        throw new WrongArgumentsAmountException();
    }

    @Override
    public String getDescription() {
        return "exit : завершить программу (без сохранения в файл)";
    }

    @Override
    public String toString() {
        return "exit";
    }

}
