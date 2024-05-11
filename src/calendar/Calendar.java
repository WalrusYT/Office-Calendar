package calendar;

import calendar.Event.Priority;
import calendar.user.User;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;

public interface Calendar {
    CalendarResponse addAccount(String name, User.Type type);
    Iterator<User> listAccounts();
    CalendarResponse addEvent(String userName, String eventName, Priority Priority, LocalDateTime date, Set<String> topics);

    enum CalendarResponse {
        ACCOUNT_ALREADY_EXISTS, ACCOUNT_REGISTERED, ACCOUNT_DOES_NOT_EXIST,
        CANNOT_CREATE_ANY, CANNOT_CREATE_HIGH, EVENT_EXISTS, IS_BUSY, OK;
    }
}
