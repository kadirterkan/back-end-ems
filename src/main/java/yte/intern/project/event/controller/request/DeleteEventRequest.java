package yte.intern.project.event.controller.request;

import lombok.Getter;
import yte.intern.project.event.enums.EventType;

@Getter
public class DeleteEventRequest {

    public DeleteEventRequest(Long id, EventType eventType) {
        this.id = id;
        this.eventType = eventType;
    }

    private final Long id;

    private final EventType eventType;
}
