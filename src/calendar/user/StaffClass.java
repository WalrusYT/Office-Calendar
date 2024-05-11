package calendar.user;

import calendar.Event;
import calendar.Event.Priority;

public class StaffClass extends UserClass implements Staff{
    public StaffClass(String name) {
        super(name);
    }

    @Override
    public Type getType() {
        return Type.STAFF;
    }

    @Override
    public CreateEventResponse promoteEvent(Event event) {
        if (event.getPriority() == Priority.HIGH) return CreateEventResponse.CANNOT_CREATE_HIGH;
        if (events.containsKey(name)) return CreateEventResponse.EVENT_EXISTS;
        if (isBusy(event.getDate())) return CreateEventResponse.IS_BUSY;
        events.put(event.getName(), event);
        return CreateEventResponse.OK;
    }
}
