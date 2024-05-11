package calendar;

import calendar.user.GuestClass;
import calendar.user.ManagerClass;
import calendar.user.StaffClass;
import calendar.user.User;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Set;

public class CalendarClass implements Calendar{
    SortedMap<String, User> accounts = new TreeMap<>();
    private boolean hasAccount(String name){
        return accounts.containsKey(name);
    }
    @Override
    public CalendarResponse addAccount(String name, User.Type type) {
        if (hasAccount(name)) return CalendarResponse.ACCOUNT_ALREADY_EXISTS;
        User user = switch (type) {
            case STAFF -> new StaffClass(name);
            case MANAGER -> new ManagerClass(name);
            case GUEST -> new GuestClass(name);
        };
        accounts.put(name, user);
        return CalendarResponse.ACCOUNT_REGISTERED;
    }

    @Override
    public CalendarResponse addEvent(String userName, String eventName, Event.Priority priority, LocalDateTime date, Set<String> topics){
        if (!hasAccount(userName)) return CalendarResponse.ACCOUNT_DOES_NOT_EXIST;
        User user = accounts.get(userName);
        Event event = new EventClass(eventName, user, priority, date, topics);
        return switch (user.promoteEvent(event)) {
            case CANNOT_CREATE_ANY -> CalendarResponse.CANNOT_CREATE_ANY;
            case CANNOT_CREATE_HIGH -> CalendarResponse.CANNOT_CREATE_HIGH;
            case EVENT_EXISTS -> CalendarResponse.EVENT_EXISTS;
            case IS_BUSY -> CalendarResponse.IS_BUSY;
            case OK -> CalendarResponse.OK;
        };
    }

    @Override
    public Iterator<User> listAccounts() {
        return accounts.values().iterator();
    }
}
