package calendar.exceptions;

/**
 * Exception if guest tries to create an event
 */
public class EventCreationForbiddenException extends CalendarException {
    public EventCreationForbiddenException(String name) {
        super("Guest account %s cannot create events.".formatted(name));
    }
}
