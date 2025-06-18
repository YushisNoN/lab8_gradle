package models;

public class IncorrectIntegerValueException extends Exception {
    public IncorrectIntegerValueException() {
        super();
    }

    @Override
    public String getMessage() {
        return "Число не должно содержать в себе других символов кроме цифр";
    }
}
