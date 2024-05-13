package calendar.user;

import calendar.Event;
import calendar.exceptions.CalendarException;
import calendar.exceptions.EventCreationForbiddenException;

public class Guest extends UserClass {
    public Guest(String name) {
        super(name);
    }

    @Override
    public Type getType() {
        return Type.GUEST;
    }

    @Override
    public void promoteEvent(Event event) throws CalendarException {
        throw new EventCreationForbiddenException(this.name);
    }
}
