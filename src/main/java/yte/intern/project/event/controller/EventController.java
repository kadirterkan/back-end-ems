package yte.intern.project.event.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/controller")
public class EventController {

    @GetMapping("/test")
    public String test(){
        return "THIS IS A TEST";
    }

}