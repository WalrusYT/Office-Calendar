package calendar.user;

import calendar.Event;
import calendar.exceptions.CalendarException;
import calendar.exceptions.EventCreationForbiddenException;

public class Guest extends UserClass {
    /**
     * Constructs a guest user with the specific name
     * @param name name of the guest user
     */
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
