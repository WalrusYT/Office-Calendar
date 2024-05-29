package calendar.user;

import java.util.Iterator;

import calendar.Calendar;
import calendar.Event;
import calendar.exceptions.CalendarException;
import calendar.exceptions.UnknownPriorityException;
import calendar.exceptions.UnknownTypeException;
import java.util.List;

public interface User extends Comparable<User> {
    /**
     * Returns a name of the user
     * @return a name of the user
     */
    String getName();

    /**
     * Returns a type of the user
     * @return a type of the user
     */
    Type getType();

    /**
     * Promotes an event for the user with the given event
     * @param event event that should be promoted by the user
     * @throws CalendarException EventCreationForbiddenException if it's a GUEST account
     * EventHighCreationForbiddenException if it's a HIGH priority of STAFF account
     * EventAlreadyExistsException if the event is already exist in the account
     */
    void promoteEvent(Event event) throws CalendarException;

    /**
     * Returns an iterator for the list of the events of the account
     * @return an iterator for the list of the events of the account
     */
    Iterator<Event> getEvents();

    /**
     * Add invitation to the accounts and returns a {@link List<Event>} of the cancelled events
     * @param event event that should be added to the account
     * @return a {@link List<Event>} of the cancelled events
     * @throws CalendarException AlreadyInvitedException if the user is already has this event
     * in the invitation list
     * AlreadyHasAnEventException if the user already attends another event
     */
    List<Event> addInvitation(Event event) throws CalendarException;

    /**
     * Make a response to the invitation and returns a {@link List<Event>} of the cancelled events
     * @param event event that should be responded
     * @param responseType type of the response
     * @return a {@link List<Event>} of the cancelled events
     * @throws CalendarException UserNotInvitedException if the user is not invited
     * @pre user should be invited to the event
     */
    List<Event> response(Event event, Calendar.Response responseType) throws CalendarException;

    /**
     * Removes an invitation from the user
     * @param event that should be removed from the user
     */
    void removeInvitation(Event event);

    /**
     * Returns a promoted event with the given name
     * @param name name of the event
     * @return a promoted event with the given name
     */
    Event getPromotedEvent(String name);

    /**
     * Enumeration for the type of the account
     */
    enum Type {
        STAFF, MANAGER, GUEST;

        /**
         * Returns a type of the user with the given {@link String} type
         * @param type - type of the user as a {@link String}
         * @return <code>MANAGER</code> if the string is "manager" and <code>STAFF</code>
         * if the string is "staff" and <code>GUEST</code> in case of "guest".
         * @throws UnknownTypeException if there is an unknown type of user.
         */
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
