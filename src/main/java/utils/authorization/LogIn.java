package utils.authorization;

import client.Client;
import models.EmptyValueException;
import models.IncorrectIntegerValueException;
import models.IncorrectStringValueException;
import models.User;
import transfer.Request;
import transfer.Response;
import utils.console.ConsoleHandler;

import java.io.IOException;

public class LogIn extends AuthForm {
    private ConsoleHandler consoleHandler;
    private boolean isLogin = false;

    private Client client = new Client("localhost", 2205, false);

    public LogIn(ConsoleHandler consoleHandler) {
        this.consoleHandler = consoleHandler;
    }

    public LogIn() {
        super();
    }

    public String askUsername() {
        boolean isPass = false;
        String currentInput = null;
        do {
            try {
                this.consoleHandler.printString("Введите имя пользователя:\n-> ");
                currentInput = this.consoleHandler.getInputString().trim();
                if (currentInput.isEmpty()) {
                    throw new EmptyValueException();
                }
                if (currentInput.matches(".*\\d.*")) {
                    throw new IncorrectStringValueException();
                }
                isPass = true;
            } catch (IncorrectStringValueException e) {
                this.consoleHandler.printStringln(e.getMessage());
            } catch (EmptyValueException e) {
                this.consoleHandler.printStringln(e.getMessage());
            }
        } while(false == isPass);
        return currentInput;
    }
    public String askPassword() {
        boolean isPass = false;
        String currentInput = null;
        do {
            try {
                this.consoleHandler.printString("Введите пароль:\n-> ");
                currentInput = this.consoleHandler.getInputString().trim();
                if (currentInput.isEmpty()) {
                    throw new EmptyValueException();
                }
                isPass = true;
            } catch (EmptyValueException e) {
                this.consoleHandler.printStringln(e.getMessage());
            }
        } while(false == isPass);
        return currentInput;
    }
    public void logIn(User user) {
        try {
            this.client.open();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        boolean pass = false;
        do {
            try {
                this.username = this.askUsername();
                Request request = new Request();
                user.setUsername(this.username);
                user.setStatus("login");
                request.setUser(user);
                this.client.sendRequest(request);
                Response response = (Response) this.client.receiveResponse(5000);
                if (response != null && response.getResponse().equals("OK")) {
                    this.password = this.askPassword();
                    user.setPassword(this.password);
                    request.setUser(user);
                    this.client.sendRequest(request);
                    response = (Response) this.client.receiveResponse(5000);
                    if(response != null && response.getResponse().equals("ACCEPT")) {
                        this.consoleHandler.printStringln("Вход успешно выполнен");
                        pass = true;
                        break;
                    }
                } else {
                    this.consoleHandler.printStringln("Пользователя с таким именем не существует");
                }
            } catch (IOException e) {
                this.consoleHandler.printStringln(e.getMessage());
            } catch (ClassNotFoundException e) {
                this.consoleHandler.printStringln(e.getMessage());
            }
        }
        while(pass == false);
    }
}
