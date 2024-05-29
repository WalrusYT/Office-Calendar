package calendar.exceptions;

/**
 * Exception if there is no user with a given name in a system
 */
public class UserNotFoundException extends CalendarException {
    public UserNotFoundException(String name) {
        super("Account %s does not exist.".formatted(name));
    }
}
