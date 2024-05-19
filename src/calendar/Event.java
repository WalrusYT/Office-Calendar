package calendar;

import calendar.exceptions.CalendarException;
import calendar.exceptions.UnknownPriorityException;
import calendar.user.User;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;

public interface Event {

    Duration EVENT_DURATION = Duration.of(1, ChronoUnit.HOURS);

    String getName();

    LocalDateTime getDate();

    Priority getPriority();

    User getPromoter();

    int getUnanswered();

    int getAccepted();

    int getRejected();

    int getInvited();

    Iterator<Event> invite(User user) throws CalendarException;

    void respond(User user, InvitationStatus status) throws CalendarException;

    void remove();

    Iterator<Event> response(User user, Calendar.Response responseType) throws CalendarException;

    enum InvitationStatus {
        REJECTED, ACCEPTED, UNANSWERED;
        public static InvitationStatus fromResponse (Calendar.Response response) {
            switch (response){
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
