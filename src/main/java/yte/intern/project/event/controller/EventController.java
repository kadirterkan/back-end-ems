package yte.intern.project.event.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.event.controller.request.*;
import yte.intern.project.event.service.CustomEventService;
import yte.intern.project.user.loginjwt.configuration.AuthenticationFacade;

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

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/list")
    public List<EventQueryResponse> getAllEvents(){
        return customEventService.getAllEvents()
                .stream()
                .map(EventQueryResponse::new)
                .toList();
    }

    @PreAuthorize("hasAuthority('MODERATOR')")
    @GetMapping("/mod/list")
    public List<EventQueryResponse> getUsersEvents(){
        return customEventService.getUsersEvents()
                .stream()
                .map(EventQueryResponse::new)
                .toList();
    }

    @PreAuthorize("hasAuthority('MODERATOR')")
    @PostMapping("/addevent")
    public MessageResponse addEventToDb(@RequestBody @Valid AddEventRequest addEventRequest){
        return customEventService.addEventToDb(addEventRequest);
    }

    @PreAuthorize("hasAuthority('MODERATOR')")
    @PostMapping("/editevent")
    public MessageResponse editEvent(@RequestBody @Valid UpdateEventRequest updateEventRequest){
        return customEventService.editEvent(updateEventRequest);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/addeventuser")
    public MessageResponse addEventToUser(@RequestBody AddEventToUserRequest addEventToUserRequest){
        String eventName = addEventToUserRequest.getEventName();
        return customEventService.addUserToEvent(eventName);
    }

    @PreAuthorize("hasAuthority('MODERATOR')")
    @PostMapping("/delete")
    public MessageResponse deleteEvent(@RequestBody DeleteEventRequest deleteEventRequest){
        return customEventService.deleteEvent(deleteEventRequest);
    }
}