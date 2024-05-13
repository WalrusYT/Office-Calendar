package calendar.exceptions;

public class UnknownTypeException extends Exception {
    public UnknownTypeException() {
        super("Unknown account type.");
    }
}
