package calendar.exceptions;

/**
 * Exception, if the given account type is unknown in the system
 */
public class UnknownTypeException extends Exception {
    public UnknownTypeException() {
        super("Unknown account type.");
    }
}
