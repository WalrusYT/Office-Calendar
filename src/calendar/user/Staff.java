package calendar.user;

import calendar.Event;
import calendar.Event.Priority;
import calendar.Event.InvitationStatus;
import calendar.exceptions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Staff extends UserClass {
    /**
     * Constructs a staff user with the specific name
     * @param name name of the staff user
     */
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

    /**
     * Rejects from the invitations when invited to the HIGH event
     * @param event event that user is invited in
     * @return list of the rejected events
     * @throws CalendarException AlreadyHasAnEventException if there is an event with a HIGH
     * priority in a staff account
     */
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

    /**
     * Removes promoted events if the invitation to the given event is more important
     * @param event event that user is invited in
     * @return NULL or the event that he removed
     */
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
