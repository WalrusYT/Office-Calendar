package calendar.exceptions;

public class AlreadyInvitedException extends CalendarException{
    public AlreadyInvitedException(String name) {
        super("Account %s was already invited.".formatted(name));
    }
}
