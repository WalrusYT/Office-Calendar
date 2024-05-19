package calendar;

import calendar.exceptions.AlreadyAnsweredException;
import calendar.exceptions.CalendarException;
import calendar.exceptions.UserNotInvitedException;
import calendar.user.User;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
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
    public User getPromoter() {
        return promoter;
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
    public Iterator<Event> invite(User user) throws CalendarException {
        invitedUsers.put(user, InvitationStatus.UNANSWERED);
        unanswered++;
        return user.addInvitation(this);
    }

    @Override
    public void respond(User user, InvitationStatus status) throws CalendarException {
        if (!invitedUsers.containsKey(user)) throw new UserNotInvitedException(user.getName());
        switch (status) {
            case ACCEPTED -> accepted++;
            case REJECTED -> rejected++;
            default -> throw new CalendarException("");
        }
        unanswered--;
        invitedUsers.put(user, status);
    }

    @Override
    public void remove() {
        for (User user : invitedUsers.keySet())
            user.removeInvitation(this);
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
    public Iterator<Event> response(User user, Calendar.Response responseType) throws CalendarException {
        if (invitedUsers.get(user) != InvitationStatus.UNANSWERED) throw new AlreadyAnsweredException(user.getName());
        respond(user, InvitationStatus.fromResponse(responseType));
        return user.response(this, responseType);
    }

    @Override
    public Iterator<Map.Entry<User, InvitationStatus>> getInvitedUsers() {
        return invitedUsers.entrySet().iterator();
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
