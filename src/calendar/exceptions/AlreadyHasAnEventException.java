package calendar.exceptions;

/**
 * Exception if the user is already attending another event
 */
public class AlreadyHasAnEventException extends CalendarException {
    public AlreadyHasAnEventException(String name){
        super("Account %s already attending another event.".formatted(name));
    }
}
