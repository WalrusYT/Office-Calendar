package calendar.user;

import java.util.Iterator;

import calendar.Event;
import calendar.exceptions.CalendarException;
import calendar.exceptions.UnknownTypeException;

public interface User extends Comparable<User> {

    String getName();

    Type getType();

    void promoteEvent(Event event) throws CalendarException;

    Iterator<Event> getPromotedEvents();

    Iterator<Event> addInvitation(Event event) throws CalendarException;

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
