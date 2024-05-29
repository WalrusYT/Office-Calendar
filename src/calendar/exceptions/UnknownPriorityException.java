package calendar.exceptions;

/**
 * Exception if the given priority is unknown in the system
 */
public class UnknownPriorityException extends Exception {
    public UnknownPriorityException() {
        super("Unknown priority type.");
    }
}
