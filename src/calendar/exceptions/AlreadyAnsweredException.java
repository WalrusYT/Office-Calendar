package calendar.exceptions;

public class AlreadyAnsweredException extends CalendarException {
    public AlreadyAnsweredException(String name){
        super("Account %s has already responded.".formatted(name));
    }
}
