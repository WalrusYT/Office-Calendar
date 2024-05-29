package calendar;

import calendar.Event.Priority;
import calendar.exceptions.CalendarException;
import calendar.exceptions.UnknownEventResponseException;
import calendar.exceptions.UnknownPriorityException;
import calendar.exceptions.UnknownTypeException;
import calendar.user.User;
import calendar.Event.InvitationStatus;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 * This interface represents an Office Calendar.
 */
public interface Calendar {
    /**
     * Returns an {@link Event} event with the given promoter name and name of the event
     * @param promoter name of the promoter of the event
     * @param eventName name of the promoted event
     * @return {@link Event} event with the given promoter name and name of the event
     */
    Event getEvent(String promoter, String eventName);

    /**
     * Adds an account to the calendar with the given name and type
     * @param name name of the account
     * @param type type of the account
     * @throws CalendarException UserAlreadyExistsException if there is a user with this name
     * @throws UnknownTypeException if the type of user is not STAFF, MANAGER or GUEST
     * @pre user shouldn't already exist and the type of user should be valid
     */
    void addAccount(String name, String type) throws CalendarException, UnknownTypeException;

    /**
     * Returns a list of the accounts in the system
     * @return a list of the accounts in the system
     */
    Iterator<User> listAccounts();

    /**
     * Adds an event to the system with the given promoter name, name of the event, priority,
     * date and list of topics
     * @param userName name of the event's promoter
     * @param eventName name of the event
     * @param Priority priority of the event
     * @param date date of the event
     * @param topics list of the topics of the event
     * @throws CalendarException if there is no user with the given name
     * @pre a promoter should be registered in the system
     */
    void addEvent(
            String userName, String eventName, Priority Priority,
            LocalDateTime date, List<String> topics
    ) throws CalendarException;

    /**
     * Returns an iterator of the list of the events of the given user
     * @param userName name of the user, whose events should be shown
     * @return an iterator of the list of the events of the given user
     * @throws CalendarException if there is no user with the given name
     * @pre a user should be registered in the system
     */
    Iterator<Event> userEvents(String userName) throws CalendarException;

    /**
     * Invites a given user to the given event (by their names) and returns (if applicable) the
     * iterator of the cancelled invitations according the system rules
     * @param invitee name of the invitee user
     * @param promoter name of the promoter user
     * @param eventName name of the event
     * @return an iterator of cancelled invitations or <code>NULL</code> if there is no
     * cancelled invitations
     * @throws CalendarException UserNotFoundException if either invitee or a promoter does not
     * exist in the system, EventNotFoundException if there is no event with the given name
     * @pre invitee and promoter users with the given event should exist in the system
     */
    Iterator<Event> inviteToEvent(String invitee, String promoter, String eventName)
            throws CalendarException;

    /**
     * Make a response for the invitation and returns (if applicable) the
     * iterator of the cancelled invitations according the system rules
     * @param invitee name of the invitee user
     * @param promoter name of the promoter user
     * @param eventName name of the event
     * @param response type of the response that should be given
     * @return an iterator of cancelled invitations or <code>NULL</code> if there is no
     * cancelled invitations
     * @throws CalendarException UserNotFoundException if either invitee or a promoter does not
     * exist in the system, EventNotFoundException if there is no event with the given name
     * @pre invitee and promoter users with the given event should exist in the system
     */
    Iterator<Event> response(String invitee, String promoter, String eventName, Response response)
            throws CalendarException;

    /**
     * Shows detailed information of an event, returns an iterator of the invitation list of the
     * given event
     * @param promoter promoter name of the event
     * @param eventName name of the event
     * @return an iterator of the invitation list of the given event
     * @throws CalendarException UserNotFoundException is a promoter does not exist in the system
     * and EventNotFoundException if there is no event with the given name
     * @pre promoter user with the given event should exist in the system
     */
    Iterator<Map.Entry<User, InvitationStatus>> event(String promoter, String eventName)
            throws CalendarException;

    /**
     * Returns an iterator of the events that have given topics
     * @param topics list of the topics
     * @return an iterator of the events that have given topics
     */
    Iterator<Event> topics(List<String> topics);

    /**
     * Enumeration that is used for storing responses as constants
     */
    enum Response {
        ACCEPT, REJECT, NO_ANSWER;

        /**
         * Returns a priority of the event with the given {@link String} type
         * @param response - priority of the event as a {@link String}
         * @return <code>ACCEPT</code> if the string is "accept" and <code>REJECT</code>
         * if the string is "reject"
         * @throws UnknownEventResponseException if there is an unknown response
         */
        public static Response fromName(String response) throws UnknownEventResponseException {
            switch (response.toLowerCase()){
                case "accept" -> { return ACCEPT; }
                case "reject" -> { return REJECT; }
            }
            throw new UnknownEventResponseException();
        }
        /**
         * Returns a response with the given invitation status
         * @param status of the invitation
         * @return <code>ACCEPT</code> if users <code>ACCEPTED</code>s the invitation and
         * <code>REJECTED</code> otherwise.
         */
        public static Response fromStatus(InvitationStatus status) {
            return switch (status) {
                case ACCEPTED -> ACCEPT;
                case REJECTED -> REJECT;
                case UNANSWERED -> NO_ANSWER;
            };
        }
    }
}
