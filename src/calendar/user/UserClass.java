package calendar.user;

import calendar.Event;
import calendar.Event.InvitationStatus;
import calendar.exceptions.*;

import java.time.LocalDateTime;
import java.util.*;

public abstract class UserClass implements User {
    protected final String name;
    protected final Map<String, Event> promotedEvents;
    protected final Map<Event, InvitationStatus> invitedTo;

    public UserClass(String name) {
        this.name = name;
        promotedEvents = new LinkedHashMap<>();
        invitedTo = new LinkedHashMap<>();
    }

    protected boolean isBusy(LocalDateTime dateTime) {
        for (Event event : promotedEvents.values())
            if (dateOverlapsEvent(dateTime, event.getDate())) return true;
        for (Map.Entry<Event, InvitationStatus> entry : invitedTo.entrySet()) {
            Event event = entry.getKey();
            InvitationStatus invitationStatus = entry.getValue();
            if (invitationStatus != InvitationStatus.ACCEPTED) continue;
            if (dateOverlapsEvent(dateTime, event.getDate())) return true;
        }
        return false;
    }

    protected boolean dateOverlapsEvent(LocalDateTime date, LocalDateTime eventDate) {
        LocalDateTime eventEnd = eventDate.plus(Event.EVENT_DURATION);
        return date.isAfter(eventDate) && date.isBefore(eventEnd);
    }

    @Override
    public void removeInvitation(Event event) {
        invitedTo.remove(event);
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
    public Iterator<Event> getPromotedEvents() {
        return promotedEvents.values().iterator();
    }

    @Override
    public void promoteEvent(Event event) throws CalendarException {
        if (promotedEvents.containsKey(event.getName()))
            throw new EventAlreadyExistsException(event.getName(), this.name);
        if (this.isBusy(event.getDate())) throw new UserBusyException(this.name);
        promotedEvents.put(event.getName(), event);
    }

    @Override
    public Event getPromotedEvent(String name) {
        return promotedEvents.get(name);
    }

    @Override
    public Iterator<Event> addInvitation(Event event) throws CalendarException {
        if (invitedTo.containsKey(event)) throw new AlreadyInvitedException(this.name);
        if (this.isBusy(event.getDate())) throw new AlreadyHasAnEventException(this.name);
        event.invite(this);
        return null;
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
