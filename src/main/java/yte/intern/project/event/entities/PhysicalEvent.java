package yte.intern.project.event.entities;

import lombok.Getter;
import yte.intern.project.event.controller.request.UpdatePhysicalEvent;
import yte.intern.project.event.enums.EventPrivacy;
import yte.intern.project.event.enums.EventType;
import yte.intern.project.user.entities.CustomMod;
import yte.intern.project.user.enums.Departments;

import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
public class PhysicalEvent extends CustomEvent {

    public PhysicalEvent() {

    }

    public PhysicalEvent(Long id,
                         Long quota,
                         String eventName,
                         String base64Image,
                         String eventCategory,
                         Departments eventPrivacy,
                         String eventDescription,
                         String eventDetail,
                         LocalDateTime startTime,
                         LocalDateTime endTime,
                         CustomMod createdBy,
                         List<String> questions,
                         Long lat,
                         Long lng,
                         String eventLocationName) {
        super(id,
                quota,
                eventName,
                base64Image,
                eventCategory,
                eventPrivacy,
                eventDescription,
                eventDetail,
                startTime,
                endTime,
                createdBy,
                questions);
        this.lat = lat;
        this.lng = lng;
        this.eventLocationName = eventLocationName;
    }

    public PhysicalEvent(Long quota,
                         String eventName,
                         String base64Image,
                         String eventCategory,
                         Departments eventPrivacy,
                         String eventDescription,
                         String eventDetail,
                         LocalDateTime startTime,
                         LocalDateTime endTime,
                         CustomMod createdBy,
                         List<String> questions,
                         Long lat,
                         Long lng,
                         String eventLocationName) {
        super(quota,
                eventName,
                base64Image,
                eventCategory,
                eventPrivacy,
                eventDescription,
                eventDetail,
                startTime,
                endTime,
                createdBy,
                questions);
        this.lat = lat;
        this.lng = lng;
        this.eventLocationName = eventLocationName;
    }

    private Long lat;
    private Long lng;

    private String eventLocationName;

    public void updateOnlineEvent(UpdatePhysicalEvent updatePhysicalEvent){
        this.quota = updatePhysicalEvent.getQuota();
        this.eventName = updatePhysicalEvent.getEventName();
        this.base64Image = updatePhysicalEvent.getBase64Image();
        this.eventDetail = updatePhysicalEvent.getEventDetail();
        this.lat = updatePhysicalEvent.getLat();
        this.lng = updatePhysicalEvent.getLng();
        this.eventLocationName = updatePhysicalEvent.getEventLocationName();
        this.startTime = updatePhysicalEvent.getStartTime();
        this.endTime = updatePhysicalEvent.getEndTime();
    }
}