package calendar;

import calendar.user.User;

import java.time.LocalDateTime;

public interface Event {

    String getName();
    LocalDateTime getDate();
    Priority getPriority();
    int getUnanswered();
    int getAccepted();
    int getRejected();
    int getInvited();

    enum EventStatus {
        INVITED, REJECTED, ACCEPTED, UNANSWERED
    }

    EventStatus getStatus(User user);
    enum Priority {
        MID, HIGH;

        public static Priority fromName(String type) throws UnknownPriorityException{
            switch (type.toLowerCase()) {
                case "high" -> { return HIGH; }
                case "mid" -> { return MID; }
            }
            throw new UnknownPriorityException();
        }
        public static class UnknownPriorityException extends Exception {
            private final static String MSG = "Unknown priority type.";
            public UnknownPriorityException() {
                super(MSG);
            }
        }
    }
}
