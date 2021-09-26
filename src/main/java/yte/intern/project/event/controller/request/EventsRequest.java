package yte.intern.project.event.controller.request;

import lombok.Getter;
import yte.intern.project.event.enums.EventType;

@Getter
public class EventsRequest {

    private final EventType eventType;

    private final int page;

    private final int units;

    private final String sortBy;

    public EventsRequest(EventType eventType, int page, int units, String sortBy) {
        this.eventType = eventType;
        this.page = page;
        this.units = units;
        this.sortBy = sortBy;
    }
}
