package calendar;

import calendar.exceptions.UnknownPriorityException;
import calendar.user.User;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;

public interface Event {

    TemporalAmount EVENT_DURATION = Duration.of(1, ChronoUnit.HOURS);

    String getName();

    LocalDateTime getDate();

    Priority getPriority();

    int getUnanswered();

    int getAccepted();

    int getRejected();

    int getInvited();

    void invite(User user);

    void accept(User user);

    void reject(User user);

    enum InvitationStatus {
        REJECTED, ACCEPTED, UNANSWERED
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
