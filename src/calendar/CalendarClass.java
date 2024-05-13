package calendar;

import calendar.exceptions.*;
import calendar.user.Guest;
import calendar.user.Manager;
import calendar.user.Staff;
import calendar.user.User;

import java.time.LocalDateTime;
import java.util.*;

public class CalendarClass implements Calendar {

    Map<String, User> accounts = new TreeMap<>();

    @Override
    public void addAccount(String name, User.Type type) throws CalendarException {
        if (accounts.containsKey(name)) throw new UserAlreadyExistsException(name);
        User user = switch (type) {
            case STAFF -> new Staff(name);
            case MANAGER -> new Manager(name);
            case GUEST -> new Guest(name);
        };
        accounts.put(name, user);
    }

    @Override
    public void addEvent(String userName, String eventName, Event.Priority priority, LocalDateTime date, Set<String> topics) throws CalendarException {
        User user = accounts.get(userName);
        if (user == null) throw new UserNotFoundException(userName);
        Event event = new EventClass(eventName, user, priority, date, topics);
        user.promoteEvent(event);
    }

    @Override
    public Iterator<User> listAccounts() {
        return accounts.values().iterator();
    }

    @Override
    public Iterator<Event> userEvents(String userName) throws CalendarException {
        User user = accounts.get(userName);
        if (user == null) throw new UserNotFoundException(userName);
        return user.getPromotedEvents();
    }

    @Override
    public Iterator<Event> inviteToEvent(String invitee, String promoter, String eventName) throws CalendarException {
        User inviteeUser = accounts.get(invitee), promoterUser = accounts.get(promoter);
        if (inviteeUser == null) throw new UserNotFoundException(invitee);
        if (promoterUser == null) throw new UserNotFoundException(promoter);
        Event event = promoterUser.getPromotedEvent(eventName);
        if (event == null) throw new EventNotFoundException(eventName, promoterUser.getName());
        return inviteeUser.addInvitation(event);
    }
}
