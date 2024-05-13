package calendar.exceptions;

public class UnknownPriorityException extends Exception {
    public UnknownPriorityException() {
        super("Unknown priority type.");
    }
}
