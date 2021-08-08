package yte.intern.project.event.controller.request;

import lombok.Getter;
import yte.intern.project.event.entities.CustomEvent;

import java.time.LocalDateTime;

@Getter
public class EventQueryResponse {

    private final Long id;
    private final String title;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final Long quota;
    private final Long attending;

    public EventQueryResponse(final CustomEvent customEvent){
        this.id=customEvent.getId();
        this.title =customEvent.getEventName();
        this.start =customEvent.getStartTime();
        this.end =customEvent.getEndTime();
        this.quota= customEvent.getQuota();
        this.attending= (long) customEvent.getUsers().size();
    }
}
