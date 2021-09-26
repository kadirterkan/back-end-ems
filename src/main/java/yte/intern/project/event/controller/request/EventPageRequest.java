package yte.intern.project.event.controller.request;

import lombok.Getter;
import yte.intern.project.event.entities.OnlineEvent;
import yte.intern.project.event.entities.PhysicalEvent;
import yte.intern.project.event.enums.Status;
import yte.intern.project.user.entities.CustomMod;
import yte.intern.project.user.enums.Departments;

import java.time.LocalDateTime;
import java.util.List;


@Getter
public class EventPageRequest {

    private final Long eventId;
    private final String eventName;
    private final Long quota;
    private final Long going;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String eventDescription;
    private final String username;
    private final String modImage;
    private final Departments modDepartment;
    private final Departments departments;
    private final String eventUrl;
    private final String eventLocationName;
    private final Long lat;
    private final Long lng;
    private final String Base64Image;

    private final String eventDetails;
    private final String eventCategory;
    private final Status status;
    private final List<String> questions;

    public EventPageRequest(PhysicalEvent physicalEvent,Status status){
        this.eventId = physicalEvent.getId();
        this.eventName = physicalEvent.getEventName();
        this.startTime = physicalEvent.getStartTime();
        this.endTime = physicalEvent.getEndTime();
        this.quota = physicalEvent.getQuota();
        this.going = (long) physicalEvent.getUsers().size();
        this.Base64Image = physicalEvent.getBase64Image();
        this.eventDescription = physicalEvent.getEventDescription();
        this.eventDetails = physicalEvent.getEventDetail();
        this.eventCategory = physicalEvent.getEventCategory();
        this.departments = physicalEvent.getEventPrivacy();
        this.questions = physicalEvent.getQuestions();
        this.eventUrl = null;
        this.eventLocationName = physicalEvent.getEventLocationName();
        this.lat = physicalEvent.getLat();
        this.lng = physicalEvent.getLng();
        CustomMod customMod = physicalEvent.getCreatedBy();
        this.modDepartment = customMod.getDepartments();
        this.username = customMod.username();
        this.modImage = customMod.getBase64ProfilePicture();
        this.status = status;
    }

    public EventPageRequest(OnlineEvent onlineEvent, Status status){
        this.eventId = onlineEvent.getId();
        this.eventName = onlineEvent.getEventName();
        this.startTime = onlineEvent.getStartTime();
        this.endTime = onlineEvent.getEndTime();
        this.quota = onlineEvent.getQuota();
        this.going = (long) onlineEvent.getUsers().size();
        this.Base64Image = onlineEvent.getBase64Image();
        this.eventDescription = onlineEvent.getEventDescription();
        this.eventDetails = onlineEvent.getEventDetail();
        this.eventCategory = onlineEvent.getEventCategory();
        this.departments = onlineEvent.getEventPrivacy();
        this.questions = onlineEvent.getQuestions();
        this.eventUrl = onlineEvent.getEventUrl();
        this.eventLocationName = null;
        this.lat = null;
        this.lng = null;
        CustomMod customMod = onlineEvent.getCreatedBy();
        this.modDepartment = customMod.getDepartments();
        this.username = customMod.username();
        this.modImage = customMod.getBase64ProfilePicture();
        this.status = status;
    }
}
