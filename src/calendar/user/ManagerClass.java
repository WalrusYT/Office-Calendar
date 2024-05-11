package calendar.user;

import calendar.CalendarStatus;
import calendar.Event;

public class ManagerClass extends UserClass implements Manager{
    public ManagerClass(String name) {
        super(name);
    }

    @Override
    public Type getType() {
        return Type.MANAGER;
    }

    @Override
    public CalendarStatus promoteEvent(Event event) {
        if (events.containsKey(event.getName())) return CalendarStatus.EVENT_EXISTS;
        if (this.isBusy(event.getDate())) return CalendarStatus.IS_BUSY;
        events.put(event.getName(), event);
        return CalendarStatus.OK;
    }
}
