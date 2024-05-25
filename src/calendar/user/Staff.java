package calendar.user;

import calendar.Event;
import calendar.Event.Priority;
import calendar.Event.InvitationStatus;
import calendar.exceptions.*;

import java.util.ArrayList;
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
    public List<Event> addInvitation(Event event) throws CalendarException {
        if (event.getPriority() != Priority.HIGH) return super.addInvitation(event);
        List<Event> cancelledEvents = new ArrayList<>();
        for (Map.Entry<Event, InvitationStatus> entry : invitedTo.entrySet()) {
            Event e = entry.getKey();
            InvitationStatus status = entry.getValue();
            if (status == InvitationStatus.REJECTED) continue;
            if (dateOverlapsEvent(event.getDate(), e.getDate()) && event != e) {
                if (e.getPriority() == Priority.HIGH) {
                    event.invite(this);
                    event.updateStatus(this, Event.InvitationStatus.REJECTED);
                    invitedTo.put(e, Event.InvitationStatus.REJECTED);
                    allEvents.add(event);
                    throw new AlreadyHasAnEventException(name);
                }
                invitedTo.put(e, Event.InvitationStatus.REJECTED);
                e.updateStatus(this, Event.InvitationStatus.REJECTED);
                cancelledEvents.add(e);
            }
        }
        for (Event e : promotedEvents.values()) {
            if (dateOverlapsEvent(event.getDate(), e.getDate())) {
                e.remove();
                allEvents.remove(e);
                promotedEvents.remove(e.getName());
                cancelledEvents.add(e);
                break;
            }
        }
        event.invite(this);
        event.updateStatus(this, Event.InvitationStatus.ACCEPTED);
        allEvents.add(event);
        invitedTo.put(event, Event.InvitationStatus.ACCEPTED);
        return cancelledEvents;
    }
}
