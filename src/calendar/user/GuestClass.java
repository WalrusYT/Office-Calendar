package calendar.user;

import calendar.CalendarStatus;
import calendar.Event;

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
}
