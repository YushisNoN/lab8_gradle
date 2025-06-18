package models;

public class CoordinateWrongValueException extends Exception {
    public CoordinateWrongValueException() {
        super();
    }
    @Override
    public String getMessage() {
        return "Значение не может быть меньше -852. Пожалуйста, будьте бдительны";
    }
}
