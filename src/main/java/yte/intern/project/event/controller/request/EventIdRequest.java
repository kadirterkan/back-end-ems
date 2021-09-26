package yte.intern.project.event.controller.request;

import lombok.Getter;

@Getter
public class EventIdRequest {

    private final Long eventId;

    public EventIdRequest(Long eventId) {
        this.eventId = eventId;
    }
}
