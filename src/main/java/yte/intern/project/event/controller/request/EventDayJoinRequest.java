package yte.intern.project.event.controller.request;

import lombok.Getter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
public class EventDayJoinRequest {

    private final Date date[];

    private final Long count[];

    public EventDayJoinRequest(final Map<Date,Long> hashMap) {
        this.date = hashMap.keySet().toArray(new Date[0]);
        this.count = hashMap.values().toArray(new Long[0]);
    }
}
