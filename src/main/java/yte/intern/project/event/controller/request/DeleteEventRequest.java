package yte.intern.project.event.controller.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class DeleteEventRequest {
    @Size(max = 255,message = "Etkinliğin Adı Bu Kadar Uzun Olamaz")
    @NotEmpty(message = "İsim Alanı Boş Bırakılamaz")
    private final String eventName;

    private final Long quota;

    @Future
    private final LocalDateTime startTime;

    @Future
    private final LocalDateTime endTime;
}
