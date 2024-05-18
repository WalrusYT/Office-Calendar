package calendar;

import calendar.Event.Priority;
import calendar.exceptions.CalendarException;
import calendar.exceptions.UnknownEventResponseException;
import calendar.exceptions.UnknownTypeException;
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

    Iterator<Event> response(String invitee, String promoter, String eventName, Response responseType) throws CalendarException;

    enum Response {
        ACCEPT, REJECT;
        public static Response fromName(String response) throws UnknownEventResponseException {
            switch (response.toLowerCase()){
                case "accept" -> { return ACCEPT; }
                case "reject" -> { return REJECT; }
            }
            throw new UnknownEventResponseException();
        }
    }
}
