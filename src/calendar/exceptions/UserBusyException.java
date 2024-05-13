package calendar.exceptions;

public class UserBusyException extends CalendarException {
    public UserBusyException(String name) {
        super("Account %s is busy.".formatted(name));
    }
}
