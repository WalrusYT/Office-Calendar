package calendar.exceptions;

public class EventHighCreationForbiddenException extends CalendarException {
    public EventHighCreationForbiddenException(String name) {
        super("Account %s cannot create high priority events.".formatted(name));
    }
}
