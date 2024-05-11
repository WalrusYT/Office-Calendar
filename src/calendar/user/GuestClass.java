package calendar.user;

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
    public CreateEventResponse promoteEvent(Event event) {
        return CreateEventResponse.CANNOT_CREATE_ANY;
    }
}
