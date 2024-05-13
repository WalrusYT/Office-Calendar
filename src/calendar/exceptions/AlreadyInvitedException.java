package calendar.exceptions;

public class AlreadyInvitedException extends CalendarException{
    public AlreadyInvitedException(String name) {
        super("%s was already invited".formatted(name));
    }
}
