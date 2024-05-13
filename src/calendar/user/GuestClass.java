package calendar.user;

import calendar.CalendarStatus;
import calendar.Event;
import calendar.exceptions.CalendarException;
import calendar.exceptions.InviteByGuestException;

import java.util.Iterator;

public class GuestClass extends UserClass implements Guest{
    public GuestClass(String name) {
        super(name);
    }

    @Override
    public Type getType() {
        return Type.GUEST;
    }

    @Override
    public CalendarStatus promoteEvent(Event event) {
        return CalendarStatus.CANNOT_CREATE_ANY;
    }

    @Override
    public Iterator<Event> inviteUser(User user, String eventName) throws CalendarException {
        throw new InviteByGuestException(user.getName());
    }
}
