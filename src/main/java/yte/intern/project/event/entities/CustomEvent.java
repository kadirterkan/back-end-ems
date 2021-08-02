package yte.intern.project.event.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import yte.intern.project.event.controller.request.EventRequest;
import yte.intern.project.user.entities.BaseUser;

import javax.persistence.*;
import javax.validation.constraints.Future;
import java.time.LocalDateTime;
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
        return id.equals(customEvent.id) && quota.equals(customEvent.quota) && eventName.equals(customEvent.eventName) && startTime.equals(customEvent.startTime) && endTime.equals(customEvent.endTime) && baseUserSet.equals(customEvent.baseUserSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quota, eventName, startTime, endTime, baseUserSet);
    }


    public CustomEvent(Long quota, String eventName, BaseUser createdBy, LocalDateTime startTime, LocalDateTime endTime, Set<BaseUser> baseUserSet) {
        this.quota = quota;
        this.eventName = eventName;
        this.attending=0L;
        this.createdBy = createdBy;
        this.startTime = startTime;
        this.endTime = endTime;
        this.baseUserSet = baseUserSet;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long quota;

    private String eventName;

    private Long attending;

    @ManyToOne
    @JoinColumn(name = "creater",referencedColumnName = "id")
    private BaseUser createdBy;


    @Future
    private LocalDateTime startTime;

    @Future
    private LocalDateTime endTime;


//    @ElementCollection                    TODO: Sorular eklenmeli
//    private List<String> questions = null;

    @ManyToMany(mappedBy = "customEventSet")
    private Set<BaseUser> baseUserSet;


    public void updateCustomEvent(EventRequest newCustomEvent){
        this.eventName = newCustomEvent.getEventName();
        this.startTime = newCustomEvent.getStartTime();
        this.endTime = newCustomEvent.getEndTime();
        this.quota = newCustomEvent.getQuota();
    }

    public void addUserToEvent(BaseUser baseUser){
        this.baseUserSet.add(baseUser);
    }

    public boolean isNotFull(){
        return baseUserSet.size()<quota;
    }

    public boolean deleteUserFromEvent(BaseUser baseUser){
        return baseUserSet.remove(baseUser);
    }

    @Override
    public String toString() {
        return "CustomEvent{" +
                "quota=" + quota +
                ", eventName='" + eventName + '\'' +
                ", attending=" + attending +
                ", createdBy=" + createdBy +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                "}\n";
    }
}