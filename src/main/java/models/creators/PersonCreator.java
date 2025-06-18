package models.creators;

import color.Color;
import country.Country;
import models.*;
import utils.console.ConsoleHandler;

public class PersonCreator {
    private ConsoleHandler consoleManager = new ConsoleHandler();
    private Person.PersonBuilder person = new Person.PersonBuilder();

    public PersonCreator(ConsoleHandler consoleHandler) {
        this.consoleManager = consoleHandler;
    }
    public PersonCreator() {
        this(new ConsoleHandler());
    }
    public Person createPerson() {
        try {
            person.setName(askName());
            person.setHeight(askHeight());
            person.setNationality(askNationality());
            person.setEyeColor(askEyeColor());
            person.setHairColor(askHairColor());
            person.setLocation(askLocation());
            
        } catch (NullValueException e) {
            throw new RuntimeException(e);
        } catch (NegativeValueException e) {
            throw new RuntimeException(e);
        }
        Person element = person.buildPerson();
        return element;
    }
    public String askName() {
        boolean passFlag = false;
        String currentInput = null;
        do {
            try {
                System.out.print("Введите имя владельца (не может быть пустой строкой)\n-> ");
                currentInput = this.consoleManager.getInputString().trim();
                if (currentInput.isEmpty()) {
                    throw new EmptyValueException();
                }
                if (currentInput.matches(".*\\d.*")) {
                    throw new IncorrectStringValueException();
                }
                passFlag = true;
            } catch (EmptyValueException e) {
                System.out.println(e.getMessage());
            } catch (IncorrectStringValueException e) {
                System.out.println(e.getMessage());
            }
        } while (false == passFlag);
        return currentInput;
    }

    public float askHeight() {
        boolean passFlag = false;
        String currentInput = null;
        do {
            try {
                System.out.print("Введите рост в см (рост не может быть меньше 0)\n-> ");
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
        return Float.parseFloat(currentInput);
    }

    public Country askNationality() {
        boolean passFlag = false;
        String currentInput = null;
        Country type = null;

        do {
            try {
                System.out.print("Выберите национальность\n[1]: Француз\n[2]: Индус\n[3]: Японец\n-> ");
                currentInput = this.consoleManager.getInputString().trim();
                if (currentInput.isEmpty()) {
                    throw new EmptyValueException();
                }
                if (currentInput.matches("^\\d+$") == false) {
                    throw new IncorrectIntegerValueException();
                }
                switch (Integer.parseInt(currentInput)) {
                    case 1:
                        type = Country.FRANCE;
                        passFlag = true;
                        break;
                    case 2:
                        type = Country.INDIA;
                        passFlag = true;
                        break;
                    case 3:
                        type = Country.JAPAN;
                        passFlag = true;
                        break;
                }
            } catch (EmptyValueException e) {
                System.out.println(e.getMessage());
            } catch (IncorrectIntegerValueException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: число слишком большое для данного типа");
            }
        } while (false == passFlag);
        return type;
    }

    public Color askEyeColor() {
        boolean passFlag = false;
        String currentInput = null;
        Color type = null;
        do {
            try {
                System.out.print("Выберите цвет глаз\n[1]: Карие\n[2]: Голубые\n-> ");
                currentInput = this.consoleManager.getInputString().trim();
                if (currentInput.isEmpty()) {
                    throw new EmptyValueException();
                }
                if (currentInput.matches("^\\d+$") == false) {
                    throw new IncorrectIntegerValueException();
                }
                switch (Integer.parseInt(currentInput)) {
                    case 1:
                        type = Color.BLACK;
                        passFlag = true;
                        break;
                    case 2:
                        type = Color.BLUE;
                        passFlag = true;
                        break;
                }
            } catch (EmptyValueException e) {
                System.out.println(e.getMessage());
            } catch (IncorrectIntegerValueException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: число слишком большое для данного типа");
            }
        } while (false == passFlag);
        return type;
    }

    public Color askHairColor() {
        boolean passFlag = false;
        String currentInput = null;
        Color type = null;
        do {
            try {
                System.out.print("Выберите цвет волос\n[1]: Русые\n[2]: Болотные\n[3]: Красные\n[4]: Седые\n-> ");
                currentInput = this.consoleManager.getInputString().trim();
                if (currentInput.isEmpty()) {
                    throw new EmptyValueException();
                }
                if (currentInput.matches("^\\d+$") == false) {
                    throw new IncorrectIntegerValueException();
                }
                switch (Integer.parseInt(currentInput)) {
                    case 1:
                        type = Color.ORANGE;
                        passFlag = true;
                        break;
                    case 2:
                        type = Color.GREEN;
                        passFlag = true;
                        break;
                    case 3:
                        type = Color.RED;
                        passFlag = true;
                        break;
                    case 4:
                        type = Color.WHITE;
                        passFlag = true;
                        break;
                }
            } catch (EmptyValueException e) {
                System.out.println(e.getMessage());
            } catch (IncorrectIntegerValueException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: число слишком большое для данного типа");
            }
        } while (false == passFlag);
        return type;
    }

    public Location askLocation() {
        Location location = new LocationCreator(this.consoleManager).createLocation();
        return location;
    }

    public Person getPerson() {
        return this.person.buildPerson();
    }
}
