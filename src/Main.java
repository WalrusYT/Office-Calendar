import calendar.Calendar;
import calendar.CalendarClass;
import calendar.exceptions.CalendarException;
import calendar.exceptions.UnknownEventResponseException;
import calendar.exceptions.UnknownPriorityException;
import calendar.exceptions.UnknownTypeException;
import calendar.user.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import calendar.Event;

public class Main {

    private static final DateTimeFormatter
        DT_FORMAT = DateTimeFormatter.ofPattern("yyyy MM dd HH");

    public static void main(String[] args) {
        Calendar calendar = new CalendarClass();
        Scanner in = new Scanner(System.in);
        String command;
        do {
            command = in.next().toLowerCase();
            handleCommand(in, command, calendar);
        }
        while (!command.equals(Commands.EXIT));
        in.close();

    }
    
    private static void handleCommand(Scanner in, String command, Calendar calendar) {
        switch (command) {
            case Commands.HELP -> System.out.println(Feedback.HELP);
            case Commands.REGISTER -> register(calendar, in);
            case Commands.ACCOUNTS -> listAccounts(calendar);
            case Commands.CREATE -> create(calendar, in);
            case Commands.EVENTS -> events(calendar, in);
            case Commands.INVITE -> invite(calendar, in);
            case Commands.RESPONSE -> response(calendar, in);
            case Commands.EXIT -> System.out.println(Feedback.BYE);
            default -> System.out.printf(Feedback.UNKNOWN_COMMAND, command.toUpperCase());
        }
    }

    private static void register(Calendar calendar, Scanner in) {
        String name = in.next(), type = in.next();
        try {
            User.Type userType = User.Type.fromName(type);
            calendar.addAccount(name, userType);
            System.out.printf(Feedback.ACCOUNT_REGISTERED, name);
        } catch (UnknownTypeException | CalendarException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void listAccounts(Calendar calendar){
        Iterator<User> iterator = calendar.listAccounts();
        if (!iterator.hasNext()) {
            System.out.println(Feedback.NO_ACCOUNTS);
            return;
        }
        System.out.println(Feedback.ACCOUNTS);
        while (iterator.hasNext()){
            User user = iterator.next();
            System.out.printf("%s [%s]%n", user.getName(), user.getType().name().toLowerCase());
        }
    }

    private static void create(Calendar calendar, Scanner in) {
        String userName = in.nextLine().trim(), eventName = in.nextLine().trim();
        String priorityStr = in.next();
        LocalDateTime date = LocalDateTime.parse(in.nextLine().trim(), DT_FORMAT);
        Set<String> topics = Set.of(in.nextLine().split(" "));
        try {
            Event.Priority priority = Event.Priority.fromName(priorityStr);
            calendar.addEvent(userName, eventName, priority, date, topics);
            System.out.printf(Feedback.EVENT_SCHEDULED, eventName);
        } catch (UnknownPriorityException | CalendarException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void events(Calendar calendar, Scanner in) {
        String userName = in.next();
        Iterator<Event> events;
        try {
            events = calendar.userEvents(userName);
        } catch (CalendarException e) {
            System.out.println(e.getMessage());
            return;
        }
        if (!events.hasNext()) {
            System.out.printf(Feedback.NO_EVENTS, userName);
            return;
        }
        System.out.printf(Feedback.EVENTS, userName);
        while (events.hasNext()) {
            Event event = events.next();
            System.out.printf(Feedback.EVENT, event.getName(), event.getInvited(),
                    event.getAccepted(), event.getRejected(), event.getUnanswered());
        }
    }

    private static void invite(Calendar calendar, Scanner in) {
        String invitee = in.nextLine().trim(), promoter = in.next(), eventName = in.nextLine().trim();
        Iterator<Event> cancelledEvents;
        try {
            cancelledEvents = calendar.inviteToEvent(invitee, promoter, eventName);
        } catch (CalendarException e) {
            System.out.println(e.getMessage());
            return;
        }
        if (cancelledEvents == null) {
            System.out.printf(Feedback.INVITED, invitee);
            return;
        }
        System.out.printf(Feedback.ACCEPTED, invitee);
        while (cancelledEvents.hasNext()) {
            Event event = cancelledEvents.next();
            boolean inviteePromoted = event.getPromoter().getName().equals(invitee);
            System.out.printf(inviteePromoted ? Feedback.REMOVED : Feedback.REJECTED,
                event.getName(), event.getPromoter().getName());
        }
    }

    private static void response(Calendar calendar, Scanner in) {
        String invitee = in.nextLine().trim(), promoter = in.next(),
                eventName = in.nextLine().trim(), responseStr = in.next();
        Iterator<Event> cancelledEvents;
        Calendar.Response responseType = null;
        try {
            responseType = Calendar.Response.fromName(responseStr);
        } catch (UnknownEventResponseException e) {
            System.out.println(e.getMessage());
        }
        try {
            cancelledEvents = calendar.response(invitee, promoter, eventName, responseType);
        } catch (CalendarException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.printf(Feedback.REPLIED, invitee, responseStr.toLowerCase());
        while (cancelledEvents.hasNext()) {
            Event event = cancelledEvents.next();
            System.out.printf(Feedback.REJECTED, event.getName(), event.getPromoter().getName());
        }
    }

    /**
     * Commands which allow users to interact with this program and the game
     */
    public static class Commands {
        public static final String
        EXIT = "exit", REGISTER = "register", ACCOUNTS = "accounts", CREATE = "create",
        EVENTS = "events", INVITE = "invite", RESPONSE = "response", EVENT = "event",
        TOPICS = "topics", HELP = "help";
    }

    /**
     * Feedback given by the program
     */
    public static class Feedback {
        public static final String
        HELP = String.format("""
        Available commands:
        %s - registers a new account
        %s - lists all registered accounts
        %s - creates a new event
        %s - lists all events of an account
        %s - invites an user to an event
        %s - response to an invitation
        %s - shows detailed information of an event
        %s - shows all events that cover a list of topics
        %s - shows the available commands
        %s - terminates the execution of the program""",
                Commands.REGISTER, Commands.ACCOUNTS, Commands.CREATE, Commands.EVENTS,
                Commands.INVITE, Commands.RESPONSE, Commands.EVENT, Commands.TOPICS,
                Commands.HELP, Commands.EXIT),
        BYE = "Bye!",
        UNKNOWN_COMMAND = "Unknown command %s. Type help to see available commands.%n",
        ACCOUNT_REGISTERED = "%s was registered.%n",
        NO_ACCOUNTS = "No accounts registered.",
        ACCOUNTS = "All accounts:",
        EVENT_SCHEDULED = "%s is scheduled.%n",
        EVENTS = "Account %s events:%n",
        EVENT = "%s status [invited %d] [accepted %d] [rejected %d] [unanswered %d]%n",
        NO_EVENTS = "Account %s has no events.%n",
        INVITED = "%s was invited%n",
        ACCEPTED = "%s accepted the invitation.%n",
        REJECTED = "%s promoted by %s was rejected%n",
        REMOVED = "%s promoted by %s was removed%n",
        REPLIED = "Accounts %s has replied %s to the invitation.%n";
    }

}