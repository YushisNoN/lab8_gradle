package commands.exceptions;

public class WrongArgumentsAmountException extends Exception {
    public WrongArgumentsAmountException() {
        super();
    }

    @Override
    public String getMessage() {
        return "Неправильное число аргументов для команды.";
    }
}
