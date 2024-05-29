package calendar;

import calendar.exceptions.CalendarException;
import calendar.exceptions.UnknownPriorityException;
import calendar.user.User;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.List;

/**
 * This interface represents an event in the Office Calendar.
 */
public interface Event {
    /**
     * Returns {@link List} list of topics over {@link String} of the event
     * @return list of topics of the event
     */
    List<String> getTopics();

    /**
     * Returns a name of the event
     * @return a {@link String} name of the event
     */
    String getName();

    /**
     * Returns a date and time of the event
     * @return a {@link LocalDateTime} date and time of the event
     */
    LocalDateTime getDate();

    /**
     * Returns a priority of the event
     * @return a {@link Priority} priority of the event
     */
    Priority getPriority();

    /**
     * Returns a promoter of the event
     * @return a promoter as an object {@link User} of the event
     */
    User getPromoter();

    /**
     * Return the number of unanswered invitations
     * @return the {@link Integer} number of unanswered invitations
     */
    int getUnanswered();
    /**
     * Return the number of unanswered invitations
     * @return the {@link Integer} number of accepted invitations
     */
    int getAccepted();
    /**
     * Return the number of unanswered invitations
     * @return the {@link Integer} number of rejected invitations
     */
    int getRejected();
    /**
     * Return the number of unanswered invitations
     * @return the {@link Integer} number of the invited people
     */
    int getInvited();

    /**
     * Invites a user to the event
     * @param user User that should be invited
     */
    void invite(User user);

    /**
     * Updates a status of the invitation (ACCEPT or REJECT the invitation)
     * @param user user whose status should be updated
     * @param status status that should be set to the user
     * @throws CalendarException UserNotInvitedException if a user is not invited
     * @pre user should be invited
     */
    void updateStatus(User user, InvitationStatus status) throws CalendarException;

    /**
     * Removes a user from the invitation list
     */
    void remove();

    /**
     * Response to the received invitation
     * @param user user who should response the invitation
     * @param responseType type of response (ACCEPT or REJECT)
     * @throws CalendarException UserNotInvitedException if a user is not invited
     * @pre user should be invited
     */
    void response(User user, Calendar.Response responseType) throws CalendarException;

    /**
     * Returns an iterator of an event invitations
     * @return an iterator of an event invitations
     */
    Iterator<Map.Entry<User, InvitationStatus>> getInvitations();

    /**
     * Checks if the event overlaps with another one
     * @param other other event that we need to check if overlaps
     * @return <code>true</code> if events date and time are the same, <code>false</code> otherwise
     */
    boolean overlaps(Event other);

    /**
     * Enumeration for pointing an invitation status
     */
    enum InvitationStatus {
        REJECTED, ACCEPTED, UNANSWERED;

        /**
         * Returns an invitation status with the given response
         * @param response response of the user
         * @return <code>ACCEPTED</code> if users <code>ACCEPT</code>s the invitation and
         * <code>REJECT</code> otherwise.
         */
        public static InvitationStatus fromResponse (Calendar.Response response) {
            switch (response) {
                case ACCEPT -> { return ACCEPTED; }
                case REJECT -> { return REJECTED; }
                default -> { return null; }
            }
        }
    }

    /**
     * Enumeration for pointing a priority of the event
     */
    enum Priority {
        MID, HIGH;
        /**
         * Returns a priority of the event with the given {@link String} type
         * @param type - priority of the event as a {@link String}
         * @return <code>HIGH</code> if the string is "high" and <code>MID</code>
         * if the string is "mid"
         * @throws UnknownPriorityException if there is an unknown priority
         */
        public static Priority fromName(String type) throws UnknownPriorityException {
            switch (type.toLowerCase()) {
                case "high" -> { return HIGH; }
                case "mid" -> { return MID; }
            }
            throw new UnknownPriorityException();
        }
    }
}
