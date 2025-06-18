package models.creators;


import models.*;
import uom.UnitOfMeasure;
import utils.console.ConsoleHandler;

public class ProductCreator {
    private ConsoleHandler consoleManager;
    private Product.ProductBuilder product = new Product.ProductBuilder();

    public ProductCreator() {
        this(new ConsoleHandler());
    }

    public ProductCreator(ConsoleHandler consoleHandler) {
        this.consoleManager = consoleHandler;
    }

    public Product createProduct() {
        try {
            product.setName(askName());
            product.setPrice(askPrice());
            product.setUnitOfMeasure(askUnitOfMeasure());
            product.setOwner(askOwner());
            product.setCoordinates(askCoordinates());
        } catch (EmptyValueException e) {
            System.out.println(e.getMessage());
        } catch (NegativeValueException e) {
            System.out.println(e.getMessage());
        } catch (NullValueException e) {
            System.out.println(e.getMessage());
        }
        Product element = product.buildProduct();

        return element;
    }

    public String askName() {
        boolean passFlag = false;
        String currentInput = null;
        do {
            try {
                System.out.print("Введите название продукта (не может быть пустой строкой)\n-> ");
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

    public Integer askPrice() throws NegativeValueException {
        boolean passFlag = false;
        String currentInput = null;
        Integer price = null;
        do {
            try {
                System.out.print("Введите цену товара (цена > 0)\n-> ");
                currentInput = this.consoleManager.getInputString().trim();
                if (currentInput.isEmpty()) {
                    throw new EmptyValueException();
                }
                if (currentInput.matches("^\\d+$") == false) {
                    throw new IncorrectIntegerValueException();
                }
                price = Integer.parseInt(currentInput);
                if (price.equals(0))
                    throw new NegativeValueException();
                passFlag = true;
            } catch (EmptyValueException e) {
                System.out.println(e.getMessage());
            } catch (IncorrectIntegerValueException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: число слишком большое для данного типа");
            } catch (NegativeValueException exception) {
                System.out.println(exception.getMessage());
            }
        } while (false == passFlag);
        return (int) price;
    }

    public UnitOfMeasure askUnitOfMeasure() {
        String currentInput = null;
        boolean passFlag = false;
        UnitOfMeasure type = null;
        do {
            try {
                System.out.print(
                        "Выберите тип единицы измерения:\n[1]: Миллиметры\n[2]: Сантиметры\n[3]: Метры\n[4]: Квадратные метры\n[5]: Литры\n[6]: Не добавлять :(\n-> ");
                currentInput = this.consoleManager.getInputString().trim();

                if (currentInput.isEmpty()) {
                    throw new EmptyValueException();
                }
                if (currentInput.matches("^\\d+$") == false) {
                    throw new IncorrectIntegerValueException();
                }
                switch (Integer.parseInt(currentInput)) {
                    case 1:
                        type = UnitOfMeasure.MILLILITERS;
                        passFlag = true;
                        break;
                    case 2:
                        type = UnitOfMeasure.CENTIMETERS;
                        passFlag = true;
                        break;
                    case 3:
                        type = UnitOfMeasure.METERS;
                        passFlag = true;
                        break;
                    case 4:
                        type = UnitOfMeasure.SQUARE_METERS;
                        passFlag = true;
                        break;
                    case 5:
                        type = UnitOfMeasure.LITERS;
                        passFlag = true;
                        break;
                    case 6:
                        type = null;
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

    public Person askOwner() {
        Person owner = new PersonCreator(this.consoleManager).createPerson();
        return owner;
    }

    public Coordinates askCoordinates() {
        Coordinates coordinates = new CoordinatesCreator(this.consoleManager).createCoordinates();
        return coordinates;
    }

    public Product.ProductBuilder getProduct() {
        return this.product;
    }
}
