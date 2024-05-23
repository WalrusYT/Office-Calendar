package calendar;

import java.util.Comparator;

public class EventTopicsComparator implements Comparator<Event> {

    @Override
    public int compare(Event o1, Event o2) {
        return Integer.compare(o2.getTopics().size(), o1.getTopics().size());
    }
}
