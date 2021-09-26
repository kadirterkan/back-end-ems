package yte.intern.project.event.controller.request;

import lombok.Getter;
import yte.intern.project.event.entities.CustomEvent;
import yte.intern.project.event.entities.OnlineEvent;
import yte.intern.project.event.entities.PhysicalEvent;
import yte.intern.project.event.enums.EventStatus;
import yte.intern.project.event.enums.Status;
import yte.intern.project.user.entities.CustomMod;
import yte.intern.project.user.enums.Departments;

import java.time.LocalDateTime;

@Getter
public class EventBoxResponse {

    private final Long eventId;
    private final String eventName;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Long quota;
    private final Long going;
    private final String username;
    private final Departments departments;
    private final EventStatus available;
//    private final String modImage;
//    private final Long interested;
    private final String base64Image;
    private final String eventDetails;
    private final String eventCategory;
    private Status status;

    public EventBoxResponse(final CustomEvent customEvent, Status status){
        this.eventId = customEvent.getId();
        this.eventName = customEvent.getEventName();
        this.startTime = customEvent.getStartTime();
        this.endTime = customEvent.getEndTime();
        this.quota = customEvent.getQuota();
        this.going = (long) customEvent.getUsers().size();
//        this.interested = (long) customEvent.getInterestedUsers().size();
        this.base64Image = customEvent.getBase64Image();
        this.eventDetails = customEvent.getEventDetail();
        this.eventCategory = customEvent.getEventCategory();
        CustomMod customMod = customEvent.getCreatedBy();
        this.departments = customMod.getDepartments();
        this.username = customMod.username();
        if (this.startTime.isBefore(LocalDateTime.now())) {
            if(this.endTime.isBefore(LocalDateTime.now())){
                this.available = EventStatus.STARTED;
            }else{
                this.available = EventStatus.ENDED;
            }
        } else {
            this.available = EventStatus.AVAILABLE;
        }
//        this.modImage = customMod.getBase64ProfilePicture();
        this.status = status;
    }

    public EventBoxResponse(final CustomEvent customEvent){
        this.eventId = customEvent.getId();
        this.eventName = customEvent.getEventName();
        this.startTime = customEvent.getStartTime();
        this.endTime = customEvent.getEndTime();
        this.quota = customEvent.getQuota();
        this.going = (long) customEvent.getUsers().size();
//        this.interested = (long) customEvent.getInterestedUsers().size();
        this.base64Image = customEvent.getBase64Image();
        this.eventDetails = customEvent.getEventDetail();
        this.eventCategory = customEvent.getEventCategory();
        if (this.startTime.isBefore(LocalDateTime.now())) {
            if(this.endTime.isBefore(LocalDateTime.now())){
                this.available = EventStatus.STARTED;
            }else{
                this.available = EventStatus.ENDED;
            }
        } else {
            this.available = EventStatus.AVAILABLE;
        }
        CustomMod customMod = customEvent.getCreatedBy();
        this.departments = customMod.getDepartments();
        this.username = customMod.username();
//        this.modImage = customMod.getBase64ProfilePicture();
    }

    public EventBoxResponse(final PhysicalEvent physicalEvent, Status status){
        this.eventId = physicalEvent.getId();
        this.eventName = physicalEvent.getEventName();
        this.startTime = physicalEvent.getStartTime();
        this.endTime = physicalEvent.getEndTime();
        this.quota = physicalEvent.getQuota();
        this.going = (long) physicalEvent.getUsers().size();
//        this.interested = (long) physicalEvent.getInterestedUsers().size();
        this.base64Image = physicalEvent.getBase64Image();
        this.eventDetails = physicalEvent.getEventDetail();
        this.eventCategory = physicalEvent.getEventCategory();
        CustomMod customMod = physicalEvent.getCreatedBy();
        this.departments = customMod.getDepartments();
        this.username = customMod.username();
        if (this.startTime.isBefore(LocalDateTime.now())) {
            if(this.endTime.isBefore(LocalDateTime.now())){
                this.available = EventStatus.STARTED;
            }else{
                this.available = EventStatus.ENDED;
            }
        } else {
            this.available = EventStatus.AVAILABLE;
        }
//        this.modImage = customMod.getBase64ProfilePicture();
        this.status = status;
    }

    public EventBoxResponse(final OnlineEvent onlineEvent, Status status){
        this.eventId = onlineEvent.getId();
        this.eventName = onlineEvent.getEventName();
        this.startTime = onlineEvent.getStartTime();
        this.endTime = onlineEvent.getEndTime();
        this.quota = onlineEvent.getQuota();
        this.going = (long) onlineEvent.getUsers().size();
//        this.interested = (long) onlineEvent.getInterestedUsers().size();
        this.base64Image = onlineEvent.getBase64Image();
        this.eventDetails = onlineEvent.getEventDetail();
        this.eventCategory = onlineEvent.getEventCategory();
        CustomMod customMod = onlineEvent.getCreatedBy();
        this.departments = customMod.getDepartments();
        if (this.startTime.isBefore(LocalDateTime.now())) {
            if(this.endTime.isBefore(LocalDateTime.now())){
                this.available = EventStatus.STARTED;
            }else{
                this.available = EventStatus.ENDED;
            }
        } else {
            this.available = EventStatus.AVAILABLE;
        }
        this.username = customMod.username();
//        this.modImage = customMod.getBase64ProfilePicture();
        this.status = status;
    }
}
