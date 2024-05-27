package calendar;

import calendar.exceptions.CalendarException;
import calendar.exceptions.UnknownPriorityException;
import calendar.user.User;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.List;

public interface Event {

    List<String> getTopics();
    
    String getName();

    LocalDateTime getDate();

    Priority getPriority();

    User getPromoter();

    int getUnanswered();

    int getAccepted();

    int getRejected();

    int getInvited();

    void invite(User user) throws CalendarException;

    void updateStatus(User user, InvitationStatus status) throws CalendarException;

    void remove();

    void response(User user, Calendar.Response responseType) throws CalendarException;

    Iterator<Map.Entry<User, InvitationStatus>> getInvitedUsers();

    boolean overlaps(Event other);

    enum InvitationStatus {
        REJECTED, ACCEPTED, UNANSWERED;
        public static InvitationStatus fromResponse (Calendar.Response response) {
            switch (response) {
                case ACCEPT -> { return ACCEPTED; }
                case REJECT -> { return REJECTED; }
                default -> { return null; }
            }
        }
    }

    enum Priority {
        MID, HIGH;

        public static Priority fromName(String type) throws UnknownPriorityException {
            switch (type.toLowerCase()) {
                case "high" -> { return HIGH; }
                case "mid" -> { return MID; }
            }
            throw new UnknownPriorityException();
        }
    }
}
