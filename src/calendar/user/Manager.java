package calendar.user;

public class Manager extends UserClass {
    public Manager(String name) {
        super(name);
    }

    @Override
    public Type getType() {
        return Type.MANAGER;
    }
}
