package calendar.user;

import calendar.CalendarStatus;
import calendar.Event;
import calendar.Event.Priority;
import calendar.exceptions.AlreadyInvitedException;
import calendar.exceptions.CalendarException;
import calendar.exceptions.NonExistentEventException;

import java.util.Iterator;

public class StaffClass extends UserClass implements Staff{
    public StaffClass(String name) {
        super(name);
    }

    @Override
    public Type getType() {
        return Type.STAFF;
    }

    @Override
    public CalendarStatus promoteEvent(Event event) {
        if (event.getPriority() == Priority.HIGH) return CalendarStatus.CANNOT_CREATE_HIGH;
        if (events.containsKey(name)) return CalendarStatus.EVENT_EXISTS;
        if (isBusy(event.getDate())) return CalendarStatus.IS_BUSY;
        events.put(event.getName(), event);
        return CalendarStatus.OK;
    }
    @Override
    public Iterator<Event> inviteUser(User user, String eventName) throws CalendarException {
        if (!events.containsKey(eventName)) throw new NonExistentEventException(user.getName(), eventName);

    }
}
