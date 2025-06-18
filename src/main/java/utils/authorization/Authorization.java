package utils.authorization;

import client.Client;
import models.IncorrectIntegerValueException;
import models.User;
import utils.console.ConsoleHandler;

public class Authorization {
    private ConsoleHandler consoleHandler = new ConsoleHandler();
    private LogIn logIn = new LogIn(this.consoleHandler);
    private SignUp signUp = new SignUp(this.consoleHandler);
    private Client client;

    private User user;

    private boolean isAuthorizated = false;

    public Authorization(User user) {
        this.user = user;
    }

    public void authorizate() {
        this.consoleHandler.printStringln("Добро пожаловать на сервер ШиЗоФрЕнИя\n\nПравила:\nНа сервере запрещено:\n1) Слушать Кулинича\n2) Задавать вопросы\n3) Писать Сенечке в тг \n4) Не поддерживать СВО");
        this.consoleHandler.printStringln("Выберите одно из предложенных действий ниже:\n[1]: Войти в аккаунт\n[2]: Зарегистрироваться\n[3]: Пойти слушать Кулинича");

        this.consoleHandler.printString("-> ");
        while(false == this.isAuthorizated && this.consoleHandler.getInputStream().hasNextLine()) {
            if(this.isAuthorizated) break;
            try {
                String currentInput = this.consoleHandler.getInputString().trim();
                if (currentInput.matches("^-?\\d+$") == false) {
                    throw new IncorrectIntegerValueException();
                }
                int choice = Integer.parseInt(currentInput);
                switch (choice) {
                    case 1:
                        this.logIn.logIn(this.user);
                        if(this.user.getPassword() != null && this.user.getUsername() != null)
                            this.isAuthorizated = true;
                        break;
                    case 2:
                        this.signUp.signUp(this.user);
                        if(this.user.getPassword() != null && this.user.getUsername() != null)
                            this.isAuthorizated = true;
                        break;
                    default:
                        this.isAuthorizated = true;
                        break;
                }
                if(this.isAuthorizated) break;
                this.consoleHandler.printStringln("Добро пожаловать на сервер ШиЗоФрЕнИя\n\nПравила:\nНа сервере запрещено:\n1) Слушать Кулинича\n2) Задавать вопросы\n3) Писать Сенечке в тг \n4) Не поддерживать СВО");
                this.consoleHandler.printStringln("Выберите одно из предложенных действий ниже:\n[1]: Войти в аккаунт\n[2]: Зарегистрироваться\n[3]: Пойти слушать Кулинича");
                this.consoleHandler.printString("-> ");
            } catch (IncorrectIntegerValueException e) {
                this.consoleHandler.printStringln(e.getMessage());
            }
        }
    }
}
