package models.creators;

import models.EmptyValueException;
import models.IncorrectIntegerValueException;
import models.Location;
import models.NullValueException;
import utils.console.ConsoleHandler;

public class LocationCreator {
    private ConsoleHandler consoleManager = new ConsoleHandler();
    private Location location = new Location();

    public LocationCreator() {
        this(new ConsoleHandler());
    }
    public LocationCreator(ConsoleHandler consoleHandler) {
        this.consoleManager = consoleHandler;
    }

    public Location createLocation() {
        try {
            location.setX(askWidth());
            location.setY(askHeight());
            location.setZ(askDepth());
        } catch (NullValueException e) {
            throw new RuntimeException(e);
        }
        return location;
    }

    public Double askHeight() {
        boolean passFlag = false;
        String currentInput = null;
        do {
            try {
                System.out.print("Введите высоту в см (рост не может быть пустой)\n-> ");
                currentInput = this.consoleManager.getInputString().trim();
                if (currentInput.isEmpty()) {
                    throw new EmptyValueException();
                }
                if (currentInput.matches("^-?\\d+(\\.\\d+)?$") == false) {
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
        return Double.parseDouble(currentInput);
    }

    public Double askDepth() {
        boolean passFlag = false;
        String currentInput = null;
        do {
            try {
                System.out.print("Введите глубину в см (рост не может быть пустой)\n-> ");
                currentInput = this.consoleManager.getInputString().trim();
                if (currentInput.isEmpty()) {
                    throw new EmptyValueException();
                }
                if (currentInput.matches("^-?\\d+(\\.\\d+)?$") == false) {
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
        return Double.parseDouble(currentInput);
    }

    public Integer askWidth() {
        boolean passFlag = false;
        String currentInput = null;
        do {
            try {
                System.out.print("Введите длину в см (рост не может быть пустой)\n-> ");
                currentInput = this.consoleManager.getInputString().trim();
                if (currentInput.isEmpty()) {
                    throw new EmptyValueException();
                }
                if (!currentInput.matches("^\\d+$")) {
                    throw new IncorrectIntegerValueException();
                }
                long value = Long.parseLong(currentInput);
                if (value > Integer.MAX_VALUE || value < Integer.MIN_VALUE) {
                    throw new NumberFormatException("Ошибка: число слишком большое для данного типа");
                }
                passFlag = true;
            } catch (EmptyValueException e) {
                System.out.println(e.getMessage());
            } catch (IncorrectIntegerValueException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        } while (!passFlag);
        return Integer.parseInt(currentInput);
    }

    public Location getLocation() {
        return this.location;
    }
}
