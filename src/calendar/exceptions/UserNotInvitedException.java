package calendar.exceptions;

public class UserNotInvitedException extends CalendarException {
    public UserNotInvitedException(String name) {
        super("Account %s is not on the invitation list.".formatted(name));
    }
}
