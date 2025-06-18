package commands.exceptions;

public class NullValueException extends Exception {
    public NullValueException() {
        super();
    }

    @Override
    public String getMessage() {
        return "Значение не может быть null";
    }
}
