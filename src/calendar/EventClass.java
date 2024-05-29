package calendar;

import calendar.exceptions.AlreadyAnsweredException;
import calendar.exceptions.CalendarException;
import calendar.exceptions.UserNotInvitedException;
import calendar.user.User;
import calendar.Calendar.Response;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * The Event class represents an event with a name, promoter, priority, date and list of topics
 */
public class EventClass implements Event {
    /**
     * Event duration
     */
    public final static Duration EVENT_DURATION = Duration.of(1, ChronoUnit.HOURS);
    /**
     * Name of the event
     */
    private final String name;
    /**
     * Name of the promoter of the event
     */
    private final User promoter;
    /**
     * Priority of the event
     */
    private final Priority priority;
    /**
     * Date and time of the event
     */
    private final LocalDateTime dateTime;
    /**
     * List of topics of the event
     */
    private final List<String> topics;
    /**
     * Map of user invitations, used to get InvitationStatus of a User in O(1) time
     */
    private final Map<User, InvitationStatus> invitations;
    /**
     * List of invited users, used to remember the sequence the invitations were sent
     */
    private final List<User> invitedUsers;
    /**
     * Numbers of unanswered, accepted and rejected invited users
     */
    private int unanswered = 0, accepted = 1, rejected = 0;

    /**
     * Constructs an object with the given name, promoter, priority, dateTime and topics
     * @param name name of the event
     * @param promoter user that promoted the event
     * @param priority priority of the event
     * @param dateTime date and time of the event
     * @param topics list of topics of the event
     */
    public EventClass(
        String name, User promoter, Priority priority,
        LocalDateTime dateTime, List<String> topics
    ) {
        this.name = name;
        this.promoter = promoter;
        this.priority = priority;
        this.dateTime = dateTime;
        this.topics = topics;
        this.invitations = new HashMap<>();
        this.invitedUsers = new ArrayList<>();
        invitations.put(promoter, InvitationStatus.ACCEPTED);
        invitedUsers.add(promoter);
    }

    @Override
    public List<String> getTopics() {
        return topics;
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
    public void invite(User user) {
        invitations.put(user, InvitationStatus.UNANSWERED);
        invitedUsers.add(user);
        unanswered++;
    }

    @Override
    public void updateStatus(User user, InvitationStatus status) throws CalendarException {
        if (!invitations.containsKey(user)) throw new UserNotInvitedException(user.getName());
        switch (status) {
            case ACCEPTED -> accepted++;
            case REJECTED -> rejected++;
            case UNANSWERED -> { return; }
        }
        unanswered--;
        invitations.put(user, status);
    }

    @Override
    public void remove() {
        for (User user : invitations.keySet())
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
    public void response(User user, Response response) throws CalendarException {
        if (!invitations.containsKey(user)) throw new UserNotInvitedException(user.getName());
        if (invitations.get(user) != InvitationStatus.UNANSWERED)
            throw new AlreadyAnsweredException(user.getName());
        InvitationStatus status = InvitationStatus.fromResponse(response);
        this.updateStatus(user, status);
    }

    @Override
    public Iterator<Map.Entry<User, InvitationStatus>> getInvitations() {
        List<Map.Entry<User, InvitationStatus>> eventInvitations = new ArrayList<>();
        for (User user : invitedUsers)
            eventInvitations.add(Map.entry(user, invitations.get(user)));
        return eventInvitations.iterator();
    }

    @Override
    public boolean overlaps(Event other) {
        Duration timeBetweenEvents = Duration.between(this.getDate(), other.getDate()).abs();
        return timeBetweenEvents.minus(EVENT_DURATION).isNegative();
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

    @Override
    public String toString() {
        return "EventClass{" +
                "name='" + name + '\'' +
                ", promoter=" + promoter +
                '}';
    }
}
