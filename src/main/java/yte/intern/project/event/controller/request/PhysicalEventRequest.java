package yte.intern.project.event.controller.request;

import lombok.Getter;
import yte.intern.project.event.dto.EventCoordinates;
import yte.intern.project.event.entities.PhysicalEvent;
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
public class PhysicalEventRequest {

    public PhysicalEventRequest(String eventName,
                                Long quota,
                                ZonedDateTime startTime,
                                ZonedDateTime endTime,
                                List<String> questions,
                                String eventDescription,
                                Departments eventPrivacy,
                                String eventCategory,
                                String base64Image,
                                EventCoordinates eventCoordinates) {
        this.eventName = eventName;
        this.quota = quota;
        this.startTime = startTime.withZoneSameInstant(ZoneId.of("Europe/Istanbul")).toLocalDateTime();
        this.endTime = endTime.withZoneSameInstant(ZoneId.of("Europe/Istanbul")).toLocalDateTime();
        this.questions = questions;
        this.eventDescription = eventDescription;
        this.eventPrivacy = eventPrivacy;
        this.eventCategory = eventCategory;
        this.eventDetail = eventCoordinates.getEventLocationName();
        this.base64Image = base64Image;
        this.eventCoordinates = eventCoordinates;
    }

    @NotEmpty
    private final String eventName;

    private final Long quota;

    @Future
    private final LocalDateTime startTime;

    @Future
    private final LocalDateTime endTime;

    private final List<String> questions;

    private final String eventDescription;

    private final Departments eventPrivacy;

    private final String eventDetail;

    private final String eventCategory;

    private final String base64Image;

    private final EventCoordinates eventCoordinates;

    public PhysicalEvent toPhysicalEvent (CustomMod creator) {
        return new PhysicalEvent(
                this.quota,
                this.eventName,
                this.base64Image,
                this.eventCategory,
                this.eventPrivacy,
                this.eventDescription,
                this.eventDetail,
                this.startTime,
                this.endTime,
                creator,
                this.questions,
                this.eventCoordinates.getLat(),
                this.eventCoordinates.getLng(),
                this.eventCoordinates.getEventLocationName()
        );
    }
}
