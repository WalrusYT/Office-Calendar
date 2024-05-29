package calendar.exceptions;

/**
 * Exception if the event already exists in account
 */
public class EventAlreadyExistsException extends CalendarException {
    public EventAlreadyExistsException(String eventName, String userName) {
        super("%s already exists in account %s.".formatted(eventName, userName));
    }
}
