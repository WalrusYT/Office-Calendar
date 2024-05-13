package calendar;

import calendar.Event.Priority;
import calendar.exceptions.CalendarException;
import calendar.user.User;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;

public interface Calendar {

    void addAccount(String name, User.Type type) throws CalendarException;

    Iterator<User> listAccounts();

    void addEvent(String userName, String eventName, Priority Priority, LocalDateTime date, Set<String> topics) throws CalendarException;

    Iterator<Event> userEvents(String userName) throws CalendarException;

    Iterator<Event> inviteToEvent(String invitee, String promoter, String eventName) throws CalendarException;
}
