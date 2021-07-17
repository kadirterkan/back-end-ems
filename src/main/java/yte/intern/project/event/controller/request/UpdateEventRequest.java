package yte.intern.project.event.controller.request;

import lombok.RequiredArgsConstructor;
import yte.intern.project.event.entities.Event;
import yte.intern.project.user.entities.User;

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

    public Event toEvent(){
        return new Event(null,
                0L,
                eventName,
                startTime,
                endTime,
//                questions, TODO: Sorular eklenecek
                new HashSet<User>());
    }
}
