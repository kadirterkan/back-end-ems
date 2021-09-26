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
public class UpdatePhysicalEvent {

//    public UpdatePhysicalEvent(Long id,
//                               String eventName,
//                               Long quota,
//                               ZonedDateTime startTime,
//                               ZonedDateTime endTime,
//                               String creator,
//                               Departments eventPrivacy,
//                               List<String> questions,
//                               String eventCategory,
//                               String base64Image,
////                               EventCoordinates eventCoordinates,
//                               String eventDescription) {
//        this.id = id;
//        this.eventName = eventName;
//        this.quota = quota;
//        this.startTime = startTime.withZoneSameInstant(ZoneId.of("Europe/Istanbul")).toLocalDateTime();
//        this.endTime = endTime.withZoneSameInstant(ZoneId.of("Europe/Istanbul")).toLocalDateTime();
////        this.creator = creator;
////        this.eventPrivacy = eventPrivacy;
////        this.questions = questions;
////        this.eventCategory = eventCategory;
//        this.base64Image = base64Image;
////        this.eventCoordinates = eventCoordinates;
////        this.eventDetail = eventCoordinates.getEventLocationName();
//        this.eventDescription = eventDescription;
//    }

    public UpdatePhysicalEvent(Long id,
                               String eventName,
                               Long quota,
                               ZonedDateTime startTime,
                               ZonedDateTime endTime,
                               String base64Image,
                               Long lat,
                               Long lng,
                               String eventLocationName,
                               String eventDetail,
                               String eventDescription) {
        this.id = id;
        this.eventName = eventName;
        this.quota = quota;
        this.startTime = startTime.withZoneSameInstant(ZoneId.of("Europe/Istanbul")).toLocalDateTime();
        this.endTime = endTime.withZoneSameInstant(ZoneId.of("Europe/Istanbul")).toLocalDateTime();
        this.base64Image = base64Image;
        this.lat = lat;
        this.lng = lng;
        this.eventLocationName = eventLocationName;
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

    private final Long lat;
    private final Long lng;

    private final String eventLocationName;

    private final String eventDetail;

    private final String eventDescription;

}
