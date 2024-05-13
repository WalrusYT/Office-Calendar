package calendar.exceptions;

public class UserAlreadyExistsException extends CalendarException {
    public UserAlreadyExistsException(String name) {
        super("%s already exists.".formatted(name));
    }
}
