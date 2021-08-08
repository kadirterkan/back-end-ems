package yte.intern.project.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.event.controller.request.*;
import yte.intern.project.event.entities.CustomEvent;
import yte.intern.project.event.service.CustomEventService;
import yte.intern.project.user.entities.CustomUser;

import javax.naming.NameNotFoundException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/controller")
public class EventController {

    private final CustomEventService customEventService;

    @Autowired
    public EventController(CustomEventService customEventService) {
        this.customEventService = customEventService;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MOD','ROLE_USER')")
    @GetMapping("/list")
    public List<EventQueryResponse> getAllEvents(){
        return customEventService.getAllEvents()
                .stream()
                .map(EventQueryResponse::new)
                .toList();
    }

    @GetMapping("/mod/list")
    public List<EventQueryResponse> getUsersEvents(){
        return customEventService.getUsersEvents()
                .stream()
                .map(EventQueryResponse::new)
                .toList();
    }

    @PreAuthorize("hasAuthority('ROLE_MOD')")
    @PostMapping("/addevent")
    public MessageResponse addEventToDb(@RequestBody @Valid EventRequest addEventRequest){
        return customEventService.addEventToDb(addEventRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_MOD')")
    @PostMapping("/editevent")
    public MessageResponse editEvent(@RequestBody @Valid UpdateEventRequest updateEventRequest){
        return customEventService.editEvent(updateEventRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/addeventuser")
    public MessageResponse addEventToUser(@RequestBody LittleEventRequest littleEventRequest) throws Exception {
        return customEventService.addUserToEvent(littleEventRequest.getEventName());
    }

    @PreAuthorize("hasAuthority('ROLE_MOD')")
    @PostMapping("/delete")
    public MessageResponse deleteEvent(@RequestBody EventRequest deleteEventRequest){
        return customEventService.deleteEvent(deleteEventRequest);
    }


    @PostMapping(value= "/qrcode" ,produces = MediaType.IMAGE_JPEG_VALUE)
    public String getQrCode(@RequestBody LittleEventRequest littleEventRequest) throws Exception {
        return customEventService.getQRCodeBase64(littleEventRequest);
    }

    @PostMapping("/getevent")
    public EventQueryResponse getEvent(@RequestBody LittleEventRequest littleEventRequest) throws NameNotFoundException {
        return new EventQueryResponse(customEventService.getEventbyeventName(littleEventRequest.getEventName()));
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/getjoinedevents")
    public List<EventQueryResponse> getJoinedEvents(){
        return customEventService.getJoinedEvents()
                .stream()
                .map(EventQueryResponse::new)
                .toList();
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/leave")
    public MessageResponse leaveEvent(@RequestBody EventIdRequest eventId) throws Exception {
        return customEventService.leaveTheEvent(eventId.getEventId());
    }

    @PreAuthorize("hasAuthority('ROLE_MOD')")
    @PostMapping("/event-users")
    public List<CustomUser> getEventJoinedUsers(@RequestBody EventIdRequest eventId) throws Exception {
        return customEventService.getEventJoinedUsers(eventId.getEventId());
    }
}