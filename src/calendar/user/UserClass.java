package calendar.user;

import calendar.Event;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class UserClass implements User {
    protected final String name;
    protected final Map<String, Event> events;

    public UserClass(String name) {
        this.name = name;
        events = new LinkedHashMap<>();
    }

    public boolean isBusy(LocalDateTime dateTime) {
        for (Event event : events.values()){
            if (event.getDate().equals(dateTime)) return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int compareTo(User u) {
        return name.compareTo(u.getName());
    }

    @Override
    public Iterator<Event> getEvents() {
        return events.values().iterator();
    }
}
