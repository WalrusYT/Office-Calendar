package calendar;

import calendar.Event.Priority;
import calendar.exceptions.CalendarException;
import calendar.exceptions.UnknownEventResponseException;
import calendar.exceptions.UnknownTypeException;
import calendar.user.User;
import calendar.Event.InvitationStatus;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public interface Calendar {
    Event getEvent(String promoter, String eventName);

    void addAccount(String name, String type) throws CalendarException, UnknownTypeException;

    Iterator<User> listAccounts();

    void addEvent(
            String userName, String eventName, Priority Priority,
            LocalDateTime date, List<String> topics
    ) throws CalendarException;

    Iterator<Event> userEvents(String userName) throws CalendarException;

    Iterator<Event> inviteToEvent(String invitee, String promoter, String eventName)
            throws CalendarException;

    Iterator<Event> response(String invitee, String promoter, String eventName, Response response)
            throws CalendarException;

    Iterator<Map.Entry<User, InvitationStatus>> event(String promoter, String eventName)
            throws CalendarException;

    Iterator<Event> topics(List<String> topics);

    enum Response {
        ACCEPT, REJECT, NO_ANSWER;

        public static Response fromName(String response) throws UnknownEventResponseException {
            switch (response.toLowerCase()){
                case "accept" -> { return ACCEPT; }
                case "reject" -> { return REJECT; }
            }
            throw new UnknownEventResponseException();
        }

        public static Response fromStatus(InvitationStatus status) {
            return switch (status) {
                case ACCEPTED -> ACCEPT;
                case REJECTED -> REJECT;
                case UNANSWERED -> NO_ANSWER;
            };
        }
    }
}
