import calendar.Calendar;
import calendar.CalendarClass;
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
            case Commands.EXIT -> System.out.println(Feedback.BYE);
            default -> System.out.printf(Feedback.UNKNOWN_COMMAND, command.toUpperCase());
        }
    }

    private static void register(Calendar calendar, Scanner in) {
        String name = in.next(), type = in.next();
        User.Type userType;
        try {
            userType = User.Type.fromName(type);
        } catch (User.Type.UnknownTypeException e) {
            System.out.println(e.getMessage());
            return;
        }
        switch (calendar.addAccount(name, userType)) {
            case ACCOUNT_REGISTERED -> System.out.printf(Feedback.ACCOUNT_REGISTERED, name);
            case ACCOUNT_ALREADY_EXISTS -> System.out.printf(Feedback.ALREADY_EXISTS, name);
            default -> System.out.println(Feedback.UNEXPECTED_ERROR);
        }
    }

    private static void listAccounts(Calendar calendar){
        Iterator<User> iterator = calendar.listAccounts();
        if (!iterator.hasNext()) { System.out.println(Feedback.NO_ACCOUNTS); return; }
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
        Event.Priority priority;
        try {
            priority = Event.Priority.fromName(priorityStr);
        } catch (Event.Priority.UnknownPriorityException e) {
            System.out.println(e.getMessage());
            return;
        }
        switch (calendar.addEvent(userName, eventName, priority, date, topics)) {
            case ACCOUNT_DOES_NOT_EXIST -> System.out.printf(Feedback.ACCOUNT_DOES_NOT_EXIST, userName);
            case CANNOT_CREATE_ANY -> System.out.printf(Feedback.CANNOT_CREATE_ANY, userName);
            case CANNOT_CREATE_HIGH -> System.out.printf(Feedback.CANNOT_CREATE_HIGH, userName);
            case EVENT_EXISTS -> System.out.printf(Feedback.EVENT_EXISTS, eventName, userName);
            case IS_BUSY -> System.out.printf(Feedback.IS_BUSY, userName);
            case OK -> System.out.printf(Feedback.EVENT_SCHEDULED, eventName);
            default -> System.out.println(Feedback.UNEXPECTED_ERROR);
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
        %s - terminates the execution of the program
        """, Commands.REGISTER, Commands.ACCOUNTS, Commands.CREATE, Commands.EVENTS,
                Commands.INVITE, Commands.RESPONSE, Commands.EVENT, Commands.TOPICS,
                Commands.HELP, Commands.EXIT),
        BYE = "Bye!",
        UNKNOWN_COMMAND = "Unknown command %s. Type help to see available commands.%n",
        ACCOUNT_REGISTERED = "%s was registered.%n",
        ALREADY_EXISTS = "%s already exists.%n",
        NO_ACCOUNTS = "No accounts registered.",
        ACCOUNTS = "All accounts:",
        ACCOUNT_DOES_NOT_EXIST = "Account %s does not exist.%n",
        CANNOT_CREATE_ANY = "Guest account %s cannot create events.%n",
        CANNOT_CREATE_HIGH = "Account %s cannot create high priority events.%n",
        EVENT_EXISTS = "%s already exists in account %s%n",
        IS_BUSY = "Account %s is busy.%n",
        EVENT_SCHEDULED = "%s is scheduled%n",
        UNEXPECTED_ERROR = "Unexpected error.";
    }

}