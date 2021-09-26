package yte.intern.project.event.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.event.controller.request.*;
import yte.intern.project.event.dto.QuestionByUser;
import yte.intern.project.event.service.CustomEventService;
import yte.intern.project.user.controller.request.UserQueryResponse;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/web-api/event-controller")
public class EventController {

    private final CustomEventService customEventService;

    @Autowired
    public EventController(CustomEventService customEventService) {
        this.customEventService = customEventService;
    }

//    @PreAuthorize("hasAuthority('ROLE_USER')")
//    @PostMapping("/join-event/{eventId}")
//    public MessageResponse joinEvent(@PathVariable("eventId") String eventIdAsString,@RequestBody EventJoinRequest eventJoinRequest){
//        Long eventId = Long.valueOf(eventIdAsString);
//        if (Objects.equals(eventJoinRequest.getEventType(), "ONLINE")) {
//            return customEventService.joinOnlineEvent(eventId,eventJoinRequest);
//        } else {
//            return customEventService.joinPhysicalEvent(eventId,eventJoinRequest);
//        }
//    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/join-event-without-answers/{eventId}")
    public MessageResponse joinEventWithoutAnswers(@PathVariable("eventId") String eventId) throws Exception {
        return customEventService.joinEventWithoutAnswers(Long.valueOf(eventId));
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/interested-event/{eventId}")
    public MessageResponse interestedEventForUser(@PathVariable("eventId") String eventIdAsString){
        return customEventService.interestedEventForUser(Long.valueOf(eventIdAsString));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_MOD')")
    @PostMapping("/event-list")
    public List<EventBoxResponse> getEvents(@RequestBody EventsRequest eventsRequest){
        return customEventService.getAllEvents(eventsRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MOD')")
    @GetMapping("/event-list/for-mods")
    public List<EventBoxResponse> getEventListForMods(){
        System.out.println("TEST");
        return customEventService.getCreatedEvents();
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/events/event-page-users/{eventId}")
    public EventPageRequest getEventPageForUsers(@PathVariable("eventId") String eventId){
        return customEventService.getEventPage(Long.valueOf(eventId));
    }

    @PreAuthorize("hasAuthority('ROLE_MOD')")
    @GetMapping("/events/event-page-mods/{eventId}")
    public EventPageRequest getEventPageForMods(@PathVariable("eventId") String eventId){
        return customEventService.getEventPageForMods(Long.valueOf(eventId));
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/events/event-list-users")
    public List<EventBoxResponse> getEventListForUser(){
        return customEventService.getEventsForUser();
    }

    @PreAuthorize("hasAuthority('ROLE_MOD')")
    @PostMapping("/addevent-physical")
    public MessageResponse addPhysicalEventToDb(@RequestBody @Valid PhysicalEventRequest addEventRequest) throws IOException {
        return customEventService.addPhysicalEventToDb(addEventRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_MOD')")
    @PostMapping("/addevent-online")
    public MessageResponse addOnlineEventToDb(@RequestBody @Valid OnlineEventRequest addEventRequest) throws IOException {
        return customEventService.addOnlineEventToDb(addEventRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_MOD')")
    @PostMapping("/editevent-physical")
    public MessageResponse editPhysicalEvent(@RequestBody @Valid UpdatePhysicalEvent updateEventRequest) throws IOException {
        return customEventService.editPhysicalEvent(updateEventRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_MOD')")
    @PostMapping("/editevent-online")
    public MessageResponse editOnlineEvent(@RequestBody @Valid UpdateOnlineEvent updateOnlineEvent){
        return customEventService.editOnlineEvent(updateOnlineEvent);
    }


    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/get-joined-events")
    public List<EventBoxResponse> getJoinedEvents(@RequestBody  EventsRequest eventsRequest){
        return customEventService.getGoingEvents(eventsRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/leave-event/{eventId}")
    public MessageResponse leaveEvent(@PathVariable("eventId") String eventIdAsString) throws Exception {
        return customEventService.leaveTheEvent(Long.valueOf(eventIdAsString));
    }

    @PreAuthorize("hasAuthority('ROLE_MOD')")
    @PostMapping("/event-users")
    public List<UserQueryResponse> getEventJoinedUsers(@RequestBody EventIdRequest eventId) throws Exception {
        return customEventService.getEventJoinedUsers(eventId.getEventId())
                .stream()
                .map(UserQueryResponse::new)
                .toList();
    }

    @PreAuthorize("hasAuthority('ROLE_MOD')")
    @PostMapping("/event-days")
    public EventDayJoinRequest getEventDays(@RequestBody EventIdRequest eventId) throws Exception {
        Map<Date, Long> eventDays = customEventService.getEventDays(eventId.getEventId());

        return new EventDayJoinRequest(eventDays);
    }

    @PreAuthorize("hasAuthority('ROLE_MOD')")
    @GetMapping("/delete-event/{eventId}")
    public MessageResponse deleteEvent(@PathVariable("eventId") String eventId){
        return customEventService.deleteEvent(Long.valueOf(eventId));
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/get-event-questions/{eventId}")
    public List<String> getQuestionsToJoin(@PathVariable("eventId") String eventId){
        System.out.println(eventId);
        return customEventService.getQuestionsToJoin(Long.valueOf(eventId));
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/ask-question/{eventId}/")
    public MessageResponse askQuestion(@PathVariable("eventId") String eventId,@RequestBody QuestionByUser question){
        return customEventService.askQuestionForEvent(question.getUserQuestion(),Long.valueOf(eventId));
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/join-event-with-answers/{eventId}")
    public MessageResponse joinEventWithAnswers(@PathVariable("eventId") String eventId,@RequestBody EventJoinRequest eventJoinRequest) throws Exception {
        return customEventService.joinEventWithAnswers(Long.valueOf(eventId),eventJoinRequest.getAnswers());
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/get-going-events")
    public List<EventBoxResponse> getGoingEvents(){
        return customEventService.getGoingEvents();
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/get-joined-events")
    public List<EventBoxResponse> getJoinedEvents(){
        return customEventService.getJoinedEvents();
    }

    @PreAuthorize("hasAuthority('ROLE_MOD')")
    @GetMapping("/get-created-events-ended")
    public List<EventBoxResponse> getCreatedEventsEnded(){
        return customEventService.getCreatedEventsEnded();
    }

    @PreAuthorize("hasAuthority('ROLE_MOD')")
    @GetMapping("/get-created-events-not-ended")
    public List<EventBoxResponse> getCreatedEventsNotEnded(){
        return customEventService.getCreatedEventsNotEnded();
    }
}