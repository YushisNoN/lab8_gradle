package models.creators;


import models.*;
import utils.console.ConsoleHandler;

public class CoordinatesCreator {
    private ConsoleHandler consoleManager = new ConsoleHandler();
    private Coordinates coordinates = new Coordinates();

    public CoordinatesCreator() {
        this(new ConsoleHandler());
    }
    public CoordinatesCreator(ConsoleHandler consoleHandler) {
        this.consoleManager = consoleHandler;
    }

    public Coordinates createCoordinates() {
        try {
            coordinates.setX(askCoordX());
            coordinates.setY(askCoordY());
        } catch (NullValueException e) {
            System.out.println(e.getMessage());
        } catch (CoordinateWrongValueException e) {
            System.out.println(e.getMessage());
        }
        return coordinates;
    }

    public Long askCoordX() {
        boolean passFlag = false;
        String currentInput = null;
        do {
            try {
                System.out.print("Введите координату Х (Х > -852)\n-> ");
                currentInput = this.consoleManager.getInputString().trim();
                if (currentInput.isEmpty()) {
                    throw new EmptyValueException();
                }
                if (currentInput.matches("^-?\\d+$") == false) {
                    throw new IncorrectIntegerValueException();
                }
                passFlag = true;
            } catch (EmptyValueException e) {
                System.out.println(e.getMessage());
            } catch (IncorrectIntegerValueException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: число слишком большое для данного типа");
            }
        } while (false == passFlag);
        return Long.parseLong(currentInput);
    }

    public Integer askCoordY() {
        boolean passFlag = false;
        String currentInput = null;
        do {
            try {
                System.out.print("Введите координату Y\n-> ");
                currentInput = this.consoleManager.getInputString().trim();
                if (currentInput.isEmpty()) {
                    throw new EmptyValueException();
                }
                if (currentInput.matches("^-?\\d+$") == false) {
                    throw new IncorrectIntegerValueException();
                }

                passFlag = true;
            } catch (EmptyValueException e) {
                System.out.println(e.getMessage());
            } catch (IncorrectIntegerValueException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: число слишком большое для данного типа");
            }
        } while (false == passFlag);
        return Integer.parseInt(currentInput);
    }

    public Coordinates getCoordinates() {
        return this.coordinates;
    }
}
