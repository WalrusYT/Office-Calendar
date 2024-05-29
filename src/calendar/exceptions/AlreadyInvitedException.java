package calendar.exceptions;

/**
 * Exception if the user was already invited
 */
public class AlreadyInvitedException extends CalendarException{
    public AlreadyInvitedException(String name) {
        super("Account %s was already invited.".formatted(name));
    }
}
