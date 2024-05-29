package calendar.exceptions;

/**
 * Exception if there is no event in the account
 */
public class EventNotFoundException extends CalendarException {
    public EventNotFoundException(String name, String eventName) {
        super("%s does not exist in account %s.".formatted(eventName, name));
    }
}
