package gui;

import javafx.util.converter.DoubleStringConverter;

public class SafeDoubleStringConverter extends DoubleStringConverter {
    @Override
    public Double fromString(String value) {
        try {
            return super.fromString(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
