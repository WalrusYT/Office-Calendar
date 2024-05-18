package calendar.exceptions;

public class UnknownEventResponseException extends Exception {
    public UnknownEventResponseException() {
        super("Unknown event response.");
    }
}
