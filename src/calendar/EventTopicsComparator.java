package calendar;

import java.util.Comparator;
import java.util.List;

/**
 * Comparator for the command <code>topics</code>
 */
public class EventTopicsComparator implements Comparator<Event> {
    /**
     * List of topics (used for comparing the number of the common ones)
     */
    private final List<String> topics;

    /**
     * Constructs an object EventTopicComparator with the given list of topics
     * @param topics list of topics
     */
    public EventTopicsComparator(List<String> topics) {
        this.topics = topics;
    }

    /**
     * Compares two events by the following order (number of common topics -> name -> promoter name)
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return the result of comparation.
     */
    @Override
    public int compare(Event o1, Event o2) {
        return Comparator.comparingInt(this::commonTopics)
                .reversed()
                .thenComparing(Event::getName)
                .thenComparing((Event e) -> e.getPromoter().getName())
                .compare(o1, o2);
    }

    /**
     * Calculates the number of common topics with the given event
     * @param event event in which we're looking for common topics
     * @return the number of common topics
     */
    private int commonTopics(Event event) {
        int amount = 0;
        for (String topic : topics)
            if (event.getTopics().contains(topic)) amount++;
        return amount;
    }
}
