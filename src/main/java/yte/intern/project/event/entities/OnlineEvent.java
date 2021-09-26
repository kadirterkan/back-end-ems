package yte.intern.project.event.entities;

import lombok.NoArgsConstructor;
import yte.intern.project.event.controller.request.UpdateOnlineEvent;
import yte.intern.project.event.enums.EventPrivacy;
import yte.intern.project.event.enums.EventType;
import yte.intern.project.user.entities.CustomMod;
import yte.intern.project.user.enums.Departments;

import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
public class OnlineEvent extends CustomEvent {


    public OnlineEvent(String eventUrl) {
        this.eventUrl = eventUrl;
    }

    public OnlineEvent(Long id,
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
                       String eventUrl) {
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
        this.eventUrl = eventUrl;
    }

    public OnlineEvent(Long quota,
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
                       String eventUrl) {
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
        this.eventUrl = eventUrl;
    }

    private String eventUrl;


    public String getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }


    public void updateOnlineEvent(UpdateOnlineEvent updateOnlineEvent){
        this.quota = updateOnlineEvent.getQuota();
        this.eventName = updateOnlineEvent.getEventName();
        this.base64Image = updateOnlineEvent.getBase64Image();
        this.eventUrl = updateOnlineEvent.getEventUrl();
        this.startTime = updateOnlineEvent.getStartTime();
        this.endTime = updateOnlineEvent.getEndTime();
    }
}