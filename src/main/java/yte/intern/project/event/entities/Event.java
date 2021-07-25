package yte.intern.project.event.entities;


import lombok.NoArgsConstructor;
import yte.intern.project.user.entities.User;

import javax.persistence.*;
import javax.validation.constraints.Future;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
public class Event {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id.equals(event.id) && quota.equals(event.quota) && eventName.equals(event.eventName) && startTime.equals(event.startTime) && endTime.equals(event.endTime) && userSet.equals(event.userSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quota, eventName, startTime, endTime, userSet);
    }

    public Event(Long id, Long quota, String eventName, LocalDateTime startTime, LocalDateTime endTime, Set<User> userSet) {
        if(startTime.isBefore(endTime)){
            this.endTime = endTime;
            this.id = id;
            this.quota = quota;
            this.eventName = eventName;
            this.startTime = startTime;
            this.userSet = userSet;
        }
        else{

        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long quota;

    private String eventName;


    @Future
    private LocalDateTime startTime;

    @Future
    private LocalDateTime endTime;


//    @ElementCollection                    TODO: Sorular eklenmeli
//    private List<String> questions = null;

    @ManyToMany(mappedBy ="eventSet")
    private Set<User> userSet;

    public void AddUserToEvent(User user){
        userSet.add(user);
    }
}