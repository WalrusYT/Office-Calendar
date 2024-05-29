package calendar.exceptions;

/**
 * Exception if the account is busy
 */
public class UserBusyException extends CalendarException {
    public UserBusyException(String name) {
        super("Account %s is busy.".formatted(name));
    }
}
