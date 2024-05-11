package calendar.user;

import calendar.Event;

public interface User extends Comparable<User> {
    String getName();
    Type getType();
    CreateEventResponse promoteEvent(Event event);
    
    enum Type {
        STAFF, MANAGER, GUEST;
        public static Type fromName(String type) throws UnknownTypeException{
            switch (type.toLowerCase()) {
                case "manager" -> { return MANAGER; }
                case "staff" -> { return STAFF; }
                case "guest" -> { return GUEST; }
            }
            throw new UnknownTypeException();
        }
        public static class UnknownTypeException extends Exception {
            private final static String MSG = "Unknown account type.";
            public UnknownTypeException() {
                super(MSG);
            }
        }
    }
    enum CreateEventResponse {
        CANNOT_CREATE_ANY, CANNOT_CREATE_HIGH, EVENT_EXISTS, IS_BUSY, OK
    }
}
