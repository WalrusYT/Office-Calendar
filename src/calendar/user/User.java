package calendar;

public interface User extends Comparable<User> {
    enum Type {
        STAFF, MANAGER, GUEST;
    }
}
