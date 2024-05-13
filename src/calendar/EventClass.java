package calendar;

import calendar.user.User;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class EventClass implements Event{
    private final String name;
    private final User promoter;
    private final Priority priority;
    private final LocalDateTime dateTime;
    private final Set<String> topics;
    private final Set<User> unanswered, accepted, rejected;

    public EventClass(
        String name, User promoter, Priority priority,
        LocalDateTime dateTime, Set<String> topics
    ) {
        this.name = name;
        this.promoter = promoter;
        this.priority = priority;
        this.dateTime = dateTime;
        this.topics = topics;
        this.unanswered = new HashSet<>();
        this.accepted = new HashSet<>();
        this.rejected = new HashSet<>();
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

    @Override
    public int getInvited() {
        return accepted.size() + rejected.size() + unanswered.size();
    }

    @Override
    public EventStatus getStatus(User user) {
        if (unanswered.contains(user)) return EventStatus.UNANSWERED;
        if (accepted.contains(user)) return EventStatus.ACCEPTED;
        if (rejected.contains(user)) return EventStatus.REJECTED;
        return null; // хз хз...
    }


    @Override
	public int getRejected() {
		return rejected.size();
	}

	@Override
	public int getAccepted() {
		return accepted.size();
	}

	@Override
	public int getUnanswered() {
		return unanswered.size();
	}
}
