package commands.exceptions;

public class ReccursionFoundException extends Exception {
    public ReccursionFoundException() {
        super();
    }

    @Override
    public String getMessage() {
        return "Обнаружено рекурсивное выполнение команд, скрипт повторно не будет запущен";
    }
}
