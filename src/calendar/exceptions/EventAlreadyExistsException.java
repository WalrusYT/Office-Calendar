package calendar.exceptions;

public class EventAlreadyExistsException extends CalendarException {
    public EventAlreadyExistsException(String eventName, String userName) {
        super("%s already exists in account %s.".formatted(eventName, userName));
    }
}
