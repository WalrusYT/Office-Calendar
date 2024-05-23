package calendar.user;

import calendar.Event;
import calendar.Event.InvitationStatus;
import calendar.exceptions.*;
import calendar.Calendar;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public abstract class UserClass implements User {
    protected final String name;
    protected final Map<String, Event> promotedEvents;
    protected final Map<Event, InvitationStatus> invitedTo;
    protected final List<Event> allEvents;

    public UserClass(String name) {
        this.name = name;
        promotedEvents = new HashMap<>();
        invitedTo = new HashMap<>();
        allEvents = new ArrayList<>();
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
        Duration diff = Duration.between(date, eventDate).abs();
        return diff.minus(Event.EVENT_DURATION).isNegative();
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
    public Iterator<Event> getEvents() {
        return allEvents.iterator();
    }

    @Override
    public void promoteEvent(Event event) throws CalendarException {
        if (promotedEvents.containsKey(event.getName()))
            throw new EventAlreadyExistsException(event.getName(), this.name);
        if (this.isBusy(event.getDate())) throw new UserBusyException(this.name);
        for (Map.Entry<Event, InvitationStatus> entry : invitedTo.entrySet()) {
            if (entry.getValue() != InvitationStatus.UNANSWERED) continue;
            Event e = entry.getKey();
            if (this.dateOverlapsEvent(event.getDate(), e.getDate())) {
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
        if (invitedTo.containsKey(event) || promotedEvents.containsValue(event)) throw new AlreadyInvitedException(name);
        if (this.isBusy(event.getDate())) throw new AlreadyHasAnEventException(this.name);
        event.invite(this);
        allEvents.add(event);
        invitedTo.put(event, Event.InvitationStatus.UNANSWERED);
        return null;
    }

    @Override
    public List<Event> response(Event event, Calendar.Response responseType) throws CalendarException {
        if (responseType == Calendar.Response.REJECT) {
            invitedTo.put(event, InvitationStatus.REJECTED);
            return null;
        }
        List<Event> cancelledEvents = new ArrayList<>();
        for (Map.Entry<Event, Event.InvitationStatus> entry : invitedTo.entrySet()) {
            Event e = entry.getKey();
            Event.InvitationStatus status = entry.getValue();
            if (dateOverlapsEvent(event.getDate(), e.getDate())) {
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
