package gui;

import javafx.util.converter.LongStringConverter;

public class SafeLongStringConverter extends LongStringConverter {
    @Override
    public Long fromString(String value) {
        try {
            return super.fromString(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
