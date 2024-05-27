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
        if (event.getPriority() == Priority.HIGH)
            throw new EventHighCreationForbiddenException(name);
        super.promoteEvent(event);
    }

    @Override
    public List<Event> addInvitation(Event event) throws CalendarException {
        if (event.getPriority() != Priority.HIGH) return super.addInvitation(event);
        List<Event> cancelledEvents = new ArrayList<>();
        List<Event> rejected = rejectInvited(event);
        Event removedEvent = removePromoted(event);
        if (removedEvent != null) cancelledEvents.add(removedEvent);
        cancelledEvents.addAll(rejected);
        event.invite(this);
        event.updateStatus(this, Event.InvitationStatus.ACCEPTED);
        allEvents.add(event);
        invitedTo.put(event, Event.InvitationStatus.ACCEPTED);
        return cancelledEvents;
    }

    protected List<Event> rejectInvited(Event event) throws CalendarException {
        List<Event> rejected = new ArrayList<>();
        for (Event e : allEvents) {
            if (promotedEvents.containsKey(e.getName())) continue;
            InvitationStatus status = invitedTo.get(e);
            if (status == InvitationStatus.REJECTED) continue;
            if (event.overlaps(e) && event != e) {
                if (e.getPriority() == Priority.HIGH) {
                    event.invite(this);
                    event.updateStatus(this, Event.InvitationStatus.REJECTED);
                    invitedTo.put(e, Event.InvitationStatus.REJECTED);
                    allEvents.add(event);
                    throw new AlreadyHasAnEventException(name);
                }
                invitedTo.put(e, Event.InvitationStatus.REJECTED);
                e.updateStatus(this, Event.InvitationStatus.REJECTED);
                rejected.add(e);
            }
        }
        return rejected;
    }

    protected Event removePromoted(Event event) {
        for (Event e : promotedEvents.values()) {
            if (event.overlaps(e)) {
                e.remove();
                allEvents.remove(e);
                promotedEvents.remove(e.getName());
                return e;
            }
        }
        return null;
    }
}
