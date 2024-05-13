package calendar.exceptions;

public class AlreadyHasAnEventException extends CalendarException {
    public AlreadyHasAnEventException(String name){
        super("%s is already attending another event.".formatted(name));
    }
}
