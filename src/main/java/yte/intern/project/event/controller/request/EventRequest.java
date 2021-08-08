package yte.intern.project.event.controller.request;

import lombok.Getter;
import yte.intern.project.event.entities.CustomEvent;
import yte.intern.project.user.entities.CustomMod;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
public class EventRequest {

    public EventRequest(String eventName,
                           Long quota,
                           ZonedDateTime startTime,
                        ZonedDateTime endTime) {
        this.eventName = eventName;
        this.quota = quota;
        this.startTime = startTime.withZoneSameInstant(ZoneId.of("Europe/Istanbul")).toLocalDateTime();
        this.endTime = endTime.withZoneSameInstant(ZoneId.of("Europe/Istanbul")).toLocalDateTime();
        if(startTime.isBefore(endTime)){
//            TODO:GIVE AN ERROR
        }
    }

    @NotEmpty
    private final String eventName;

    private final Long quota;

    @Future
    private final LocalDateTime startTime;

    @Future
    private final LocalDateTime endTime;

    public CustomEvent toEvent(CustomMod creator){
        return new CustomEvent(
                this.quota,
                this.eventName,
                creator,
                this.startTime,
                this.endTime
        );
    }
}
