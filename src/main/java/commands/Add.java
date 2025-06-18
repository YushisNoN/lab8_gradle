package commands;


import transfer.Request;
import commands.exceptions.WrongArgumentsAmountException;
import models.IncorrectStringValueException;
import models.Product;
import models.creators.ProductCreator;
import utils.console.ConsoleHandler;

public class Add extends Command {

    private Request request;
    private final ConsoleHandler consoleHandler;

    public Add(Request request, ConsoleHandler consoleHandler) {
        super();
        this.request =request;
        this.consoleHandler = consoleHandler;
    }

    @Override
    public void execute(String[] arguments) throws WrongArgumentsAmountException, IncorrectStringValueException {
        throw new WrongArgumentsAmountException();
    }

    @Override
    public String getDescription() {
        return "add {element} : добавить новый элемент в коллекцию";
    }

    @Override
    public String toString() {
        return "add";
    }

    @Override
    public void execute() throws WrongArgumentsAmountException {
        Product product = new ProductCreator(this.consoleHandler).createProduct();
        this.request.setProduct(product);
//        try {
//            this.client.sendProduct(product);
//            Object response = client.receiveResponse(5000);
//            if (response != null) {
//                if (response instanceof String) {
//                    System.out.println((String) response);
//                }
//            }
//        } catch (IOException e) {
//            System.out.println("Произошла ошибка отправки объекта. Объект не был доставлен на сервер");
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
    }
}
