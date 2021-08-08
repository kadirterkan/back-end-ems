package yte.intern.project.event.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import yte.intern.project.event.entities.CustomEvent;
import yte.intern.project.user.entities.CustomMod;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class UpdateEventRequest {
    @NotEmpty
    private final String eventName;

    private final Long quota;

    @Future
    private final LocalDateTime startTime;

    @Future
    private final LocalDateTime endTime;

    private final String oldEventName;

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
