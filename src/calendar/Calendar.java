package calendar;

import calendar.Event.Priority;
import calendar.user.User;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;

public interface Calendar {
    CalendarStatus addAccount(String name, User.Type type);
    Iterator<User> listAccounts();
    CalendarStatus addEvent(String userName, String eventName, Priority Priority, LocalDateTime date, Set<String> topics);

    record CalendarResponse<T>(T result, CalendarStatus status) {
        public CalendarResponse(T result) {
            this(result, CalendarStatus.OK);
        }

        public CalendarResponse(CalendarStatus status) {
            this(null, status);
        }
    }
}
