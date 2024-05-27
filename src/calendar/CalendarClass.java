package calendar;

import calendar.exceptions.*;
import calendar.user.Guest;
import calendar.user.Manager;
import calendar.user.Staff;
import calendar.user.User;
import calendar.Event.InvitationStatus;

import java.time.LocalDateTime;
import java.util.*;

public class CalendarClass implements Calendar {

    Map<String, User> accounts = new TreeMap<>();
    Map<String, List<Event>> topicEvents = new HashMap<>();

    @Override
    public Event getEvent(String promoter, String eventName) {
        User user = accounts.get(promoter);
        return user.getPromotedEvent(eventName);
    }

    @Override
    public void addAccount(String name, String type)
            throws CalendarException, UnknownTypeException {
        if (accounts.containsKey(name)) throw new UserAlreadyExistsException(name);
        User.Type userType = User.Type.fromName(type);
        User user = switch (userType) {
            case STAFF -> new Staff(name);
            case MANAGER -> new Manager(name);
            case GUEST -> new Guest(name);
        };
        accounts.put(name, user);
    }

    @Override
    public void addEvent(
            String userName, String eventName, Event.Priority priority,
            LocalDateTime date, List<String> topics
    ) throws CalendarException {
        User user = accounts.get(userName);
        if (user == null) throw new UserNotFoundException(userName);
        Event event = new EventClass(eventName, user, priority, date, topics);
        user.promoteEvent(event);
        for (String topic : topics) {
            topicEvents.computeIfAbsent(topic, x -> new ArrayList<>()).add(event);
        }
    }

    protected void removeEvent(Event event) {
        event.remove();
        for (List<Event> events : topicEvents.values()) {
            events.remove(event);
        }
    }

    @Override
    public Iterator<User> listAccounts() {
        return accounts.values().iterator();
    }

    @Override
    public Iterator<Event> userEvents(String userName) throws CalendarException {
        User user = accounts.get(userName);
        if (user == null) throw new UserNotFoundException(userName);
        return user.getEvents();
    }

    @Override
    public Iterator<Event> inviteToEvent(String invitee, String promoter, String eventName)
            throws CalendarException {
        User inviteeUser = accounts.get(invitee), promoterUser = accounts.get(promoter);
        if (promoterUser == null) throw new UserNotFoundException(promoter);
        if (inviteeUser == null) throw new UserNotFoundException(invitee);
        Event event = promoterUser.getPromotedEvent(eventName);
        if (event == null) throw new EventNotFoundException(promoterUser.getName(), eventName);
        List<Event> cancelledEvents = inviteeUser.addInvitation(event);
        if (cancelledEvents == null) return null;
        for (Event e : cancelledEvents)
            if (e.getPromoter() == promoterUser) removeEvent(e);
        return cancelledEvents.iterator();
    }

    @Override
    public Iterator<Event> response(
            String invitee, String promoter, String eventName, Response response
    ) throws CalendarException {
        User inviteeUser = accounts.get(invitee), promoterUser = accounts.get(promoter);
        if (inviteeUser == null) throw new UserNotFoundException(invitee);
        if (promoterUser == null) throw new UserNotFoundException(promoter);
        Event event = promoterUser.getPromotedEvent(eventName);
        if (event == null) throw new EventNotFoundException(promoterUser.getName(), eventName);
        List<Event> cancelledEvents = inviteeUser.response(event, response);
        if (cancelledEvents == null) return null;
        for (Event e : cancelledEvents)
            if (e.getPromoter() == promoterUser) removeEvent(e);
        return cancelledEvents.iterator();
    }

    @Override
    public Iterator<Map.Entry<User, InvitationStatus>> event(String promoter, String eventName)
            throws CalendarException {
        User user = accounts.get(promoter);
        if (user == null) throw new UserNotFoundException(promoter);
        Event event = user.getPromotedEvent(eventName);
        if (event == null) throw new EventNotFoundException(user.getName(), eventName);
        return event.getInvitations();
    }

    @Override
    public Iterator<Event> topics(List<String> topics) {
        Set<Event> events = new TreeSet<>(new EventTopicsComparator(topics));
        for (String topic : topics)
            if (topicEvents.containsKey(topic))
                events.addAll(topicEvents.get(topic));
        return events.iterator();
    }
}
