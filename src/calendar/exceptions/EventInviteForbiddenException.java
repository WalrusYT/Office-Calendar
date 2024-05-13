package calendar.exceptions;

public class EventInviteForbiddenException extends CalendarException{
    public EventInviteForbiddenException(String name){
        super("%s cannot invite users".formatted(name));
    }
}
