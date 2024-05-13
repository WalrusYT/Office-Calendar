package calendar.exceptions;

public class NonExistentEventException extends CalendarException{
    public NonExistentEventException (String name, String eventName) {
        super(eventName + " does not exist in account " + name);
    }
}
