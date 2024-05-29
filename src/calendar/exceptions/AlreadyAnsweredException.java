package calendar.exceptions;

/**
 * Exception if the user has already responded the invitation
 */
public class AlreadyAnsweredException extends CalendarException {
    public AlreadyAnsweredException(String name){
        super("Account %s has already responded.".formatted(name));
    }
}
