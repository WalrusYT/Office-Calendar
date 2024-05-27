package calendar;

import java.util.Comparator;
import java.util.List;

public class EventTopicsComparator implements Comparator<Event> {

    private final List<String> topics;

    public EventTopicsComparator(List<String> topics) {
        this.topics = topics;
    }

    @Override
    public int compare(Event o1, Event o2) {
        return Comparator.comparingInt(this::commonTopics)
                .reversed()
                .thenComparing(Event::getName)
                .thenComparing((Event e) -> e.getPromoter().getName())
                .compare(o1, o2);
    }

    private int commonTopics(Event event) {
        int amount = 0;
        for (String topic : topics)
            if (event.getTopics().contains(topic)) amount++;
        return amount;
    }
}
