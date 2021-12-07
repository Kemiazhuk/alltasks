package com.epam.rd.autocode.observer.git;

import java.util.ArrayList;
import java.util.List;

public class MergeWebHook implements WebHook {
    private List<Event> events = new ArrayList<>();
    private String branch;

    public MergeWebHook(String branch) {
        this.branch = branch;
    }

    @Override
    public String branch() {
        return branch;
    }

    @Override
    public Event.Type type() {
        return Event.Type.MERGE;
    }

    @Override
    public List<Event> caughtEvents() {
        List<Event> tempEvents = new ArrayList<>();
        for (Event event : events) {
            Event tempEvent = new Event(Event.Type.COMMIT, "branch", event.commits());
            tempEvents.add(tempEvent);
        }
        return tempEvents;
    }

    @Override
    public void onEvent(Event event) {
        events.add(event);
    }
}
