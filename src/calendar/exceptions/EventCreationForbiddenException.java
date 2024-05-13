package calendar.exceptions;

public class EventCreationForbiddenException extends CalendarException {
    public EventCreationForbiddenException(String name) {
        super("Guest account %s cannot create events.".formatted(name));
    }
}
