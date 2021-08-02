package yte.intern.project.event.controller.request;

import lombok.Getter;
import yte.intern.project.event.entities.CustomEvent;

import java.time.LocalDateTime;

@Getter
public class EventQueryResponse {

    private final Long id;
    private final String eventName;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Long quota;
    private final Long attending;

    public EventQueryResponse(final CustomEvent customEvent){
        this.id=customEvent.getId();
        this.eventName=customEvent.getEventName();
        this.startTime=customEvent.getStartTime();
        this.endTime=customEvent.getEndTime();
        this.quota= customEvent.getQuota();
        this.attending= (long) customEvent.getBaseUserSet().size();
    }
}
