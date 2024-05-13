package calendar.user;

import java.time.LocalDateTime;
import java.util.Iterator;

import calendar.CalendarStatus;
import calendar.Event;
import calendar.exceptions.AlreadyInvitedException;
import calendar.exceptions.CalendarException;

public interface User extends Comparable<User> {

    String getName();

    Type getType();

    CalendarStatus promoteEvent(Event event);
    
    Iterator<Event> getEvents();

    Iterator<Event> inviteUser(User user, String eventName) throws CalendarException;

    boolean isBusy(LocalDateTime dateTime);
    
    enum Type {
        STAFF, MANAGER, GUEST;
        public static Type fromName(String type) throws UnknownTypeException{
            switch (type.toLowerCase()) {
                case "manager" -> { return MANAGER; }
                case "staff" -> { return STAFF; }
                case "guest" -> { return GUEST; }
            }
            throw new UnknownTypeException();
        }
        public static class UnknownTypeException extends Exception {
            private final static String MSG = "Unknown account type.";
            public UnknownTypeException() {
                super(MSG);
            }
        }
    }
    enum CreateEventResponse {
        CANNOT_CREATE_ANY, CANNOT_CREATE_HIGH, EVENT_EXISTS, IS_BUSY, OK
    }
}
