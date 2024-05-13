package calendar.exceptions;

public class AlreadyInvitedException extends CalendarException{
    public AlreadyInvitedException(String name){
        super(name + " was already invited");
    }

}
