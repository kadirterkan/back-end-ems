package yte.intern.project.event.controller.request;

import lombok.RequiredArgsConstructor;
import yte.intern.project.event.entities.Event;
import yte.intern.project.user.entities.User;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

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

    @NotEmpty(message = "Kontenjan Kısmı Boş Bırakılamaz")
    private final Long quota;

    @Future
    @NotEmpty(message = "Başlangıç Tarihi Girilmek Zorunda")
    private final LocalDateTime startTime;

    @Future
    @NotEmpty(message = "Bitiş Tarihi Girilmek Zorunda")
    private final LocalDateTime endTime;

//    @Size(max = 255) TODO: ADD QUESTIONS IN FUTURE
//    private final List<String> questions;

    public Event toEvent(){
        return new Event(null,
                0L,
                eventName,
                startTime,
                endTime,
//                questions, TODO: Sorular eklenecek
                new HashSet<User>()
                );
    }
}
