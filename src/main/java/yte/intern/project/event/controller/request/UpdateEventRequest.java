package yte.intern.project.event.controller.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import yte.intern.project.event.entities.CustomEvent;
import yte.intern.project.user.entities.AppUser;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class UpdateEventRequest {


    @Size(max = 255,message = "Etkinliğin Adı Bu Kadar Uzun Olamaz")
    @NotEmpty(message = "İsim Alanı Boş Bırakılamaz")
    private final String eventName;

    private final Long quota;

    @Future
    private final LocalDateTime startTime;

    @Future
    private final LocalDateTime endTime;

//    private final List<String> questions;

    public CustomEvent toEvent(AppUser creator){
        return new CustomEvent(
                quota,
                eventName,
                creator,
                startTime,
                endTime,
                new HashSet<AppUser>());
    }
}
