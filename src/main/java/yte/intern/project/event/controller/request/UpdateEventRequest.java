package yte.intern.project.event.controller.request;

import lombok.RequiredArgsConstructor;
import yte.intern.project.event.entities.CustomEvent;
import yte.intern.project.user.entities.AppUser;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
public class UpdateEventRequest {

    private final String eventName;

    private final Long quota;

    private final LocalDateTime startTime;

    private final LocalDateTime endTime;

    private final List<String> questions;

    public CustomEvent toEvent(){
        return new CustomEvent(
                quota,
                eventName,
                startTime,
                endTime,
                new HashSet<AppUser>());
    }
}
