package yte.intern.project.event.entities;


import lombok.NoArgsConstructor;
import yte.intern.project.user.entities.AppUser;

import javax.persistence.*;
import javax.validation.constraints.Future;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
public class CustomEvent {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomEvent customEvent = (CustomEvent) o;
        return id.equals(customEvent.id) && quota.equals(customEvent.quota) && eventName.equals(customEvent.eventName) && startTime.equals(customEvent.startTime) && endTime.equals(customEvent.endTime) && appUserSet.equals(customEvent.appUserSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quota, eventName, startTime, endTime, appUserSet);
    }

    public CustomEvent(Long quota, String eventName, LocalDateTime startTime, LocalDateTime endTime, Set<AppUser> appUserSet) {
        this.quota = quota;
        this.eventName = eventName;
        this.attending = 0L;
        this.startTime = startTime;
        this.endTime = endTime;
        this.appUserSet = appUserSet;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long quota;

    private String eventName;

    private Long attending;


    @Future
    private LocalDateTime startTime;

    @Future
    private LocalDateTime endTime;


//    @ElementCollection                    TODO: Sorular eklenmeli
//    private List<String> questions = null;

    @ManyToMany(mappedBy = "customEventSet")
    private Set<AppUser> appUserSet;

}