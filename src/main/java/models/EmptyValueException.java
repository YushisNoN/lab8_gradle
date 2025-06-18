package models;


public class EmptyValueException extends Exception {
    public EmptyValueException() {
        super();
    }

    @Override
    public String getMessage() {
        return "Значение не может быть пустым";
    }
}
