package calendar.exceptions;

public class UserNotFoundException extends CalendarException {
    public UserNotFoundException(String name) {
        super("Account %s does not exist.".formatted(name));
    }
}
