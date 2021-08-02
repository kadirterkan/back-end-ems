package yte.intern.project.event.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import yte.intern.project.event.controller.request.EventQueryResponse;
import yte.intern.project.event.controller.request.UpdateEventRequest;
import yte.intern.project.user.entities.AppUser;

import javax.persistence.*;
import javax.validation.constraints.Future;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
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


    public CustomEvent(Long quota, String eventName, AppUser createdBy, LocalDateTime startTime, LocalDateTime endTime, Set<AppUser> appUserSet) {
        this.quota = quota;
        this.eventName = eventName;
        this.attending=0L;
        this.createdBy = createdBy;
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

    @ManyToOne
    @JoinColumn(name = "creater",referencedColumnName = "id")
    private AppUser createdBy;


    @Future
    private LocalDateTime startTime;

    @Future
    private LocalDateTime endTime;


//    @ElementCollection                    TODO: Sorular eklenmeli
//    private List<String> questions = null;

    @ManyToMany(mappedBy = "customEventSet")
    private Set<AppUser> appUserSet;


    public void updateCustomEvent(UpdateEventRequest newCustomEvent){
        this.eventName = newCustomEvent.getEventName();
        this.startTime = newCustomEvent.getStartTime();
        this.endTime = newCustomEvent.getEndTime();
        this.quota = newCustomEvent.getQuota();
    }

    public void addUserToEvent(AppUser appUser){
        this.appUserSet.add(appUser);
    }

    public boolean isNotFull(){
        return appUserSet.size()<quota;
    }

}