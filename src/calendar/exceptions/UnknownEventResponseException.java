package calendar.exceptions;

/**
 * Exception if the given response is unknown in the system
 */
public class UnknownEventResponseException extends Exception {
    public UnknownEventResponseException() {
        super("Unknown event response.");
    }
}
