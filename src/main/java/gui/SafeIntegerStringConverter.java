package gui;

import javafx.util.converter.IntegerStringConverter;

public class SafeIntegerStringConverter extends IntegerStringConverter {
    @Override
    public Integer fromString(String value) {
        try {
            return super.fromString(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
