package calendar;

import calendar.user.User;
import java.time.LocalDateTime;
import java.util.Set;

public class EventClass implements Event{
    private String name;
    private User promoter;
    private Priority priority;
    private LocalDateTime dateTime;
    private final Set<String> topics;

    public EventClass(
        String name, User promoter, Priority priority,
        LocalDateTime dateTime, Set<String> topics
    ) {
        this.name = name;
        this.promoter = promoter;
        this.priority = priority;
        this.dateTime = dateTime;
        this.topics = topics;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    @Override
    public LocalDateTime getDate(){
        return dateTime;
    }
}
