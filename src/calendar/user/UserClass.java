package calendar.user;

import calendar.Event;
import calendar.Event.InvitationStatus;
import calendar.exceptions.*;
import calendar.Calendar.Response;

import java.util.*;

public abstract class UserClass implements User {
    protected final String name;
    /**
     * Map for storing this user promoted events and getting them
     * in O(1) time by event name
     */
    protected final Map<String, Event> promotedEvents;
    /**
     * Map for storing invitation status for each event this user was invited to
     * and getting them in O(1) time by Event object
     */
    protected final Map<Event, InvitationStatus> invitedTo;
    /**
     * List for storing all events (promoted and invited to)
     * Used to remember in which order the events were added to this user
     * to remember the sequence they were added in
     */
    protected final List<Event> allEvents;

    public UserClass(String name) {
        this.name = name;
        promotedEvents = new HashMap<>();
        invitedTo = new HashMap<>();
        allEvents = new ArrayList<>();
    }

    protected boolean unableToAttend(Event event) {
        for (Event e : promotedEvents.values())
            if (event.overlaps(e)) return true;
        for (Map.Entry<Event, InvitationStatus> entry : invitedTo.entrySet()) {
            Event e = entry.getKey();
            InvitationStatus invitationStatus = entry.getValue();
            if (invitationStatus != InvitationStatus.ACCEPTED) continue;
            if (event.overlaps(e)) return true;
        }
        return false;
    }

    @Override
    public void removeInvitation(Event event) {
        invitedTo.remove(event);
        allEvents.remove(event);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int compareTo(User u) {
        return name.compareTo(u.getName());
    }

    @Override
    public Iterator<Event> getEvents() {
        return allEvents.iterator();
    }

    @Override
    public void promoteEvent(Event event) throws CalendarException {
        if (promotedEvents.containsKey(event.getName()))
            throw new EventAlreadyExistsException(event.getName(), this.name);
        if (this.unableToAttend(event)) throw new UserBusyException(this.name);
        for (Map.Entry<Event, InvitationStatus> entry : invitedTo.entrySet()) {
            if (entry.getValue() != InvitationStatus.UNANSWERED) continue;
            Event e = entry.getKey();
            if (event.overlaps(e)) {
                e.updateStatus(this, InvitationStatus.REJECTED);
                invitedTo.put(e, InvitationStatus.REJECTED);
            }
        }
        allEvents.add(event);
        promotedEvents.put(event.getName(), event);
    }

    @Override
    public Event getPromotedEvent(String name) {
        return promotedEvents.get(name);
    }

    @Override
    public List<Event> addInvitation(Event event) throws CalendarException {
        if (invitedTo.containsKey(event) || promotedEvents.containsValue(event))
            throw new AlreadyInvitedException(name);
        if (this.unableToAttend(event)) throw new AlreadyHasAnEventException(this.name);
        event.invite(this);
        allEvents.add(event);
        invitedTo.put(event, Event.InvitationStatus.UNANSWERED);
        return null;
    }

    @Override
    public List<Event> response(Event event, Response response) throws CalendarException {
        event.response(this, response);
        List<Event> cancelledEvents = new ArrayList<>();
        if (response == Response.REJECT) {
            invitedTo.put(event, InvitationStatus.REJECTED);
            return cancelledEvents;
        }
        for (Event e : allEvents) {
            Event.InvitationStatus status = invitedTo.get(e);
            if (event.overlaps(e) && event != e) {
                if (status != Event.InvitationStatus.UNANSWERED) continue;
                e.updateStatus(this, InvitationStatus.REJECTED);
                invitedTo.put(e, InvitationStatus.REJECTED);
                cancelledEvents.add(e);
            }
        }
        invitedTo.put(event, Event.InvitationStatus.ACCEPTED);
        return cancelledEvents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserClass userClass)) return false;
        return Objects.equals(name, userClass.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
