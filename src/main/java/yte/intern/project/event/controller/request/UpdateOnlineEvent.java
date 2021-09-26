package yte.intern.project.event.controller.request;

import lombok.Getter;
import yte.intern.project.event.entities.OnlineEvent;
import yte.intern.project.event.enums.EventPrivacy;
import yte.intern.project.user.entities.CustomMod;
import yte.intern.project.user.enums.Departments;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
public class UpdateOnlineEvent {

    public UpdateOnlineEvent(Long id,
                               String eventName,
                               Long quota,
                               ZonedDateTime startTime,
                               ZonedDateTime endTime,
                               String base64Image,
                               String eventUrl,
                               String eventDetail,
                               String eventDescription) {
        this.id = id;
        this.eventName = eventName;
        this.quota = quota;
        this.startTime = startTime.withZoneSameInstant(ZoneId.of("Europe/Istanbul")).toLocalDateTime();
        this.endTime = endTime.withZoneSameInstant(ZoneId.of("Europe/Istanbul")).toLocalDateTime();
        this.base64Image = base64Image;
        this.eventUrl = eventUrl;
        this.eventDetail = eventDetail;
        this.eventDescription = eventDescription;
    }

    private final Long id;

    @NotEmpty
    private final String eventName;

    private final Long quota;

    @Future
    private final LocalDateTime startTime;

    @Future
    private final LocalDateTime endTime;

    private final String base64Image;

    private final String eventUrl;

    private final String eventDetail;

    private final String eventDescription;
}
