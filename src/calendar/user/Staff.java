package calendar.user;

import calendar.Event;
import calendar.Event.Priority;
import calendar.Event.InvitationStatus;
import calendar.exceptions.*;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Staff extends UserClass {
    public Staff(String name) {
        super(name);
    }

    @Override
    public Type getType() {
        return Type.STAFF;
    }

    @Override
    public void promoteEvent(Event event) throws CalendarException {
        if (event.getPriority() == Priority.HIGH) throw new EventHighCreationForbiddenException(name);
        super.promoteEvent(event);
    }

    @Override
    public Iterator<Event> addInvitation(Event event) throws CalendarException {
        if (event.getPriority() != Priority.HIGH) return super.addInvitation(event);
        List<Event> cancelledEvents = new ArrayList<>();
        for (Event e : promotedEvents.values()) { // цикл по пустой коллекции
            if (dateOverlapsEvent(event.getDate(), e.getDate())) {
                e.remove();
                cancelledEvents.add(e);
                break;
            }
        }
        // не добавляем ивент ??
        for (Map.Entry<Event, InvitationStatus> entry : invitedTo.entrySet()) {
            Event e = entry.getKey();
            InvitationStatus status = entry.getValue();
            if (status == InvitationStatus.REJECTED) continue;
            if (dateOverlapsEvent(event.getDate(), e.getDate()))
                cancelledEvents.add(e);
        }
        return cancelledEvents.iterator();
    }
}
