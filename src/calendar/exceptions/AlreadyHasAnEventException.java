package calendar.exceptions;

public class AlreadyHasAnEventException extends CalendarException {
    public AlreadyHasAnEventException(String name){
        super(name + " is already attending another event.");
    }
}
