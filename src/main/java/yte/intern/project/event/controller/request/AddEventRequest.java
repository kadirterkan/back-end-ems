package yte.intern.project.event.controller.request;

import lombok.Getter;
import yte.intern.project.event.entities.CustomEvent;
import yte.intern.project.user.entities.AppUser;
import yte.intern.project.user.loginjwt.jwt.JWTUtil;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;

@Getter
public class AddEventRequest {

    public AddEventRequest(String eventName,
                           Long quota,
                           LocalDateTime startTime,
                           LocalDateTime endTime) {
        this.eventName = eventName;
        this.quota = quota;
        this.startTime = startTime;
        this.endTime = endTime;
        if(startTime.isBefore(endTime)){
//            TODO:GIVE AN ERROR
        }
    }

    @Size(max = 255,message = "Etkinliğin Adı Bu Kadar Uzun Olamaz")
    @NotEmpty(message = "İsim Alanı Boş Bırakılamaz")
    private final String eventName;

    private final Long quota;

    @Future
    private final LocalDateTime startTime;

    @Future
    private final LocalDateTime endTime;

//    @Size(max = 255) TODO: ADD QUESTIONS IN FUTURE
//    private final List<String> questions;

    public CustomEvent toEvent(AppUser creator){
        return new CustomEvent(quota,
                eventName,
                creator,
                startTime,
                endTime,
                new HashSet<AppUser>());
    }

}
