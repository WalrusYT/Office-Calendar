package calendar.user;

import calendar.Event;
import calendar.Event.Priority;
import calendar.Event.InvitationStatus;
import calendar.exceptions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Staff extends UserClass {
    public Staff(String name) {
        super(name);
    }

    @Override
    public Type getType() {
        return Type.STAFF;
    }

    @Override
    public void promoteEvent(Event event) throws CalendarException {
        if (event.getPriority() == Priority.HIGH) throw new EventHighCreationForbiddenException(name);
        super.promoteEvent(event);
    }

    @Override
    public Iterator<Event> addInvitation(Event event) throws CalendarException {
        if (event.getPriority() != Priority.HIGH) return super.addInvitation(event);
        List<Event> cancelledEvents = new ArrayList<>();
        for (Map.Entry<Event, InvitationStatus> entry : invitedTo.entrySet()) {
            Event e = entry.getKey();
            InvitationStatus status = entry.getValue();
            if (status == InvitationStatus.REJECTED) continue;
            if (dateOverlapsEvent(event.getDate(), e.getDate())) {
                if (e.getPriority() == Priority.HIGH)
                    throw new AlreadyHasAnEventException(name);
                invitedTo.put(e, Event.InvitationStatus.REJECTED);
                cancelledEvents.add(e);
            }
        }
        for (Event e : promotedEvents.values()) {
            if (dateOverlapsEvent(event.getDate(), e.getDate())) {
                e.remove();
                cancelledEvents.add(e);
                break;
            }
        }
        event.respond(this, Event.InvitationStatus.ACCEPTED);
        invitedTo.put(event, Event.InvitationStatus.ACCEPTED);
        return cancelledEvents.iterator();
    }
}
