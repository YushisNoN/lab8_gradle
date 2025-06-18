package models;

public class IncorrectStringValueException extends Exception {
    public IncorrectStringValueException() {
        super();
    }

    @Override
    public String getMessage() {
        return "Строка не должна содерать цифр.";
    }
}
