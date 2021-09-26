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
public class OnlineEventRequest {

    public OnlineEventRequest(String eventName,
                              Long quota,
                              ZonedDateTime startTime,
                              ZonedDateTime endTime,
                              List<String> questions,
                              String eventDescription,
                              String eventCategory,
                              Departments eventPrivacy,
                              String base64Image,
                              String eventUrl) {
        this.eventName = eventName;
        this.quota = quota;
        this.startTime = startTime.withZoneSameInstant(ZoneId.of("Europe/Istanbul")).toLocalDateTime();
        this.endTime = endTime.withZoneSameInstant(ZoneId.of("Europe/Istanbul")).toLocalDateTime();
        this.questions = questions;
        this.eventDescription = eventDescription;
        this.eventCategory = eventCategory;
        this.eventPrivacy = eventPrivacy;
        this.base64Image = base64Image;
        this.eventUrl = eventUrl;
    }

    @NotEmpty
    private final String eventName;

    private final Long quota;

    @Future
    private final LocalDateTime startTime;

    @Future
    private final LocalDateTime endTime;

    private final String eventType = "ONLINE";

    private final List<String> questions;

    private final String eventDescription;

    private final String eventCategory;

    private final Departments eventPrivacy;

    private final String base64Image;

    private final String eventUrl;

    public OnlineEvent toOnlineEvent(CustomMod creator){
        return new OnlineEvent(
                this.quota,
                this.eventName,
                this.base64Image,
                this.eventCategory,
                this.eventPrivacy,
                this.eventDescription,
                this.eventType,
                this.startTime,
                this.endTime,
                creator,
                this.questions,
                this.eventUrl
        );
    }

}
