package yte.intern.project.event.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.event.controller.request.AddEventRequest;
import yte.intern.project.event.service.CustomEventService;
import yte.intern.project.user.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/controller")
public class EventController {

    private final CustomEventService customEventService;
    private final UserService userService;

    @Autowired
    public EventController(CustomEventService customEventService, UserService userService) {
        this.customEventService = customEventService;
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/list")
    public String test(){
        return "THIS IS A TEST";
    }

    @PreAuthorize("hasAuthority('MODERATOR')")
    @PostMapping("/addevent")
    public MessageResponse addEventToDb(@RequestBody @Valid AddEventRequest addEventRequest){
        return customEventService.addEventToDb(addEventRequest);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/addeventuser")
    public MessageResponse addEventToUser(@RequestBody String userName,String eventName){
        return userService.addEventToUser(userName,eventName);
    }

}