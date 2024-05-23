package calendar.user;

import java.util.Iterator;

import calendar.Calendar;
import calendar.Event;
import calendar.exceptions.CalendarException;
import calendar.exceptions.UnknownTypeException;
import java.util.List;

public interface User extends Comparable<User> {

    String getName();

    Type getType();

    void promoteEvent(Event event) throws CalendarException;

    Iterator<Event> getEvents();

    Iterator<Event> getPromotedEvents();

    List<Event> addInvitation(Event event) throws CalendarException;

    List<Event> response(Event event, Calendar.Response responseType) throws CalendarException;

    void removeInvitation(Event event);

    Event getPromotedEvent(String name);
    
    enum Type {
        STAFF, MANAGER, GUEST;
        public static Type fromName(String type) throws UnknownTypeException {
            switch (type.toLowerCase()) {
                case "manager" -> { return MANAGER; }
                case "staff" -> { return STAFF; }
                case "guest" -> { return GUEST; }
            }
            throw new UnknownTypeException();
        }
    }
}
