package calendar;

import calendar.user.User;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class EventClass implements Event {
    private final String name;
    private final User promoter;
    private final Priority priority;
    private final LocalDateTime dateTime;
    private final Set<String> topics;
    private final Map<User, InvitationStatus> invitedUsers;
    private int unanswered = 0, accepted = 0, rejected = 0;

    public EventClass(
        String name, User promoter, Priority priority,
        LocalDateTime dateTime, Set<String> topics
    ) {
        this.name = name;
        this.promoter = promoter;
        this.priority = priority;
        this.dateTime = dateTime;
        this.topics = topics;
        this.invitedUsers = new HashMap<>();
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
        return accepted + rejected + unanswered;
    }

    @Override
    public void invite(User user) {
        invitedUsers.put(user, InvitationStatus.UNANSWERED);
        unanswered++;
    }

    @Override
    public void accept(User user) {
        invitedUsers.put(user, InvitationStatus.ACCEPTED);
        accepted++;
    }

    @Override
    public void reject(User user) {
        invitedUsers.put(user, InvitationStatus.REJECTED);
        rejected++;
    }

    @Override
	public int getRejected() {
		return rejected;
	}

	@Override
	public int getAccepted() {
		return accepted;
	}

	@Override
	public int getUnanswered() {
		return unanswered;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventClass that)) return false;
        return Objects.equals(name, that.name)
                && Objects.equals(promoter, that.promoter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, promoter);
    }
}
