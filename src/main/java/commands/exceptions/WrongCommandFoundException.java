package commands.exceptions;

public class WrongCommandFoundException extends Exception {
    public WrongCommandFoundException() {
        super();
    }

    @Override
    public String getMessage() {
        return "Неверная команда. Ознакомьтесь со списком команд : help";
    }
}
