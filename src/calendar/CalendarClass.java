package calendar;

import calendar.exceptions.AlreadyHasAnEventException;
import calendar.exceptions.AlreadyInvitedException;
import calendar.exceptions.CalendarException;
import calendar.exceptions.NonExistentEventException;
import calendar.user.GuestClass;
import calendar.user.ManagerClass;
import calendar.user.StaffClass;
import calendar.user.User;

import java.time.LocalDateTime;
import java.util.*;

public class CalendarClass implements Calendar {

    SortedMap<String, User> accounts = new TreeMap<>();
    Map<String, Event> allEvents = new LinkedHashMap<>();

    private boolean hasAccount(String name){
        return accounts.containsKey(name);
    }

    @Override
    public CalendarStatus addAccount(String name, User.Type type) {
        if (hasAccount(name)) return CalendarStatus.ACCOUNT_ALREADY_EXISTS;
        User user = switch (type) {
            case STAFF -> new StaffClass(name);
            case MANAGER -> new ManagerClass(name);
            case GUEST -> new GuestClass(name);
        };
        accounts.put(name, user);
        return CalendarStatus.ACCOUNT_REGISTERED;
    }

    @Override
    public CalendarStatus addEvent(String userName, String eventName, Event.Priority priority, LocalDateTime date, Set<String> topics){
        if (!hasAccount(userName)) return CalendarStatus.NO_ACCOUNT;
        User user = accounts.get(userName);
        Event event = new EventClass(eventName, user, priority, date, topics);
        allEvents.put(eventName, event);
        return user.promoteEvent(event);
    }

    @Override
    public Iterator<User> listAccounts() {
        return accounts.values().iterator();
    }

    @Override
    public CalendarResponse<Iterator<Event>> userEvents(String userName) {
        User user = accounts.get(userName);
        if (user == null) return new CalendarResponse<>(CalendarStatus.NO_ACCOUNT);
        return new CalendarResponse<>(user.getEvents());
    }

    @Override
    public CalendarResponse<Iterator<Event>> inviteToEvent(String invitee, String promoter, String eventName) throws CalendarException {
        if (!hasAccount(invitee) || !hasAccount(promoter)) {
            return new CalendarResponse<>(CalendarStatus.NO_ACCOUNT);
        }
        User inviteeUser = accounts.get(invitee), promoterUser = accounts.get(promoter);
        Event event = allEvents.get(eventName);
        if (event == null) return null; // не написано бля шо делать
        if (inviteeUser.isBusy(event.getDate())) throw new AlreadyHasAnEventException(invitee);
        if (event.getStatus(inviteeUser) != null) throw new AlreadyInvitedException(invitee);
        promoterUser.inviteUser(inviteeUser, eventName); // хз
    }
}
