package calendar.exceptions;

public class UserAlreadyExistsException extends CalendarException {
    public UserAlreadyExistsException(String name) {
        super("Account %s already exists.".formatted(name));
    }
}
