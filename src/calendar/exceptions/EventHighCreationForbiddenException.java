package calendar.exceptions;

/**
 * Exception if user without a permission tries to create a HIGH priority event
 */
public class EventHighCreationForbiddenException extends CalendarException {
    public EventHighCreationForbiddenException(String name) {
        super("Account %s cannot create high priority events.".formatted(name));
    }
}
