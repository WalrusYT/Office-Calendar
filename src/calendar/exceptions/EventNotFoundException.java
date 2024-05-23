package calendar.exceptions;

public class EventNotFoundException extends CalendarException {
    public EventNotFoundException(String name, String eventName) {
        super("%s does not exist in account %s.".formatted(eventName, name));
    }
}
