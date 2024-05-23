package calendar.exceptions;

public class AlreadyHasAnEventException extends CalendarException {
    public AlreadyHasAnEventException(String name){
        super("Account %s already attending another event.".formatted(name));
    }
}
