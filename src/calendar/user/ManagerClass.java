package calendar.user;

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
    public CreateEventResponse promoteEvent(Event event) {
        if (events.containsKey(event.getName())) return CreateEventResponse.EVENT_EXISTS;
        if (this.isBusy(event.getDate())) return CreateEventResponse.IS_BUSY;
        events.put(event.getName(), event);
        return CreateEventResponse.OK;
    }
}
