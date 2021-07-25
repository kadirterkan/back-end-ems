package yte.intern.project.event.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/controller")
public class EventController {

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping("/list")
    public String test(){
        return "THIS IS A TEST";
    }

}