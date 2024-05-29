package calendar.exceptions;

/**
 * Exception, if user is already registered in the system
 */
public class UserAlreadyExistsException extends CalendarException {
    public UserAlreadyExistsException(String name) {
        super("Account %s already exists.".formatted(name));
    }
}
