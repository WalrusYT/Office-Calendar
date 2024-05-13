package calendar.exceptions;

public class InviteByGuestException extends CalendarException{
    public InviteByGuestException(String name){
        super(name + " cannot invite users");
    }
}
