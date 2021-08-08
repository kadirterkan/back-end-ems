package yte.intern.project.event.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import yte.intern.project.common.entities.UserEvent;
import yte.intern.project.event.controller.request.EventRequest;
import yte.intern.project.event.controller.request.UpdateEventRequest;
import yte.intern.project.user.entities.CustomMod;
import yte.intern.project.user.entities.CustomUser;

import javax.persistence.*;
import javax.validation.constraints.Future;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
public class CustomEvent {


    public CustomEvent(Long quota,
                       String eventName,
                       CustomMod createdBy,
                       LocalDateTime startTime,
                       LocalDateTime endTime) {
        this.quota = quota;
        this.eventName = eventName;
        this.createdBy = createdBy;
        this.startTime = startTime;
        this.endTime = endTime;
        this.users =new HashSet<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long quota;

    private String eventName;


    @ManyToOne
    @JoinColumn(name = "creater",referencedColumnName = "id")
    private CustomMod createdBy;


    @Future
    private LocalDateTime startTime;

    @Future
    private LocalDateTime endTime;


//    @ElementCollection                    TODO: Sorular eklenmeli
//    private List<String> questions = null;

//    @ManyToMany(mappedBy = "customEventSet")
//    private Set<CustomUser> customUserSet;

    @OneToMany
    @MapsId("event")
    private Set<UserEvent> users;



    public void updateCustomEvent(UpdateEventRequest newCustomEvent){
        this.eventName = newCustomEvent.getEventName();
        this.startTime = newCustomEvent.getStartTime();
        this.endTime = newCustomEvent.getEndTime();
        this.quota = newCustomEvent.getQuota();
    }

    public void addUserToEvent(UserEvent userEvent){
        this.users.add(userEvent);
    }

    public boolean isNotFull(){
        return users.size()<quota;
    }

    public boolean deleteUserFromEvent(UserEvent userEvent){
        return users.remove(userEvent);
    }

    @Override
    public String toString() {
        return "CustomEvent{" +
                "quota=" + quota +
                ", eventName='" + eventName + '\'' +
                ", createdBy=" + createdBy +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", currently people attending" + users.size() +
                "}\n";
    }
}