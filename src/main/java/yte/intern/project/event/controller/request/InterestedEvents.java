package yte.intern.project.event.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import yte.intern.project.event.enums.EventType;

@Getter
@AllArgsConstructor
public class InterestedEvents {

    private final Long id;

    private final EventType eventType;
}
