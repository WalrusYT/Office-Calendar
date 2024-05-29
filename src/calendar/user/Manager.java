package calendar.user;

public class Manager extends UserClass {
    /**
     * Constructs a manager user with the specific name
     * @param name name of the manager user
     */
    public Manager(String name) {
        super(name);
    }

    @Override
    public Type getType() {
        return Type.MANAGER;
    }
}
