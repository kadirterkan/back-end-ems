package yte.intern.project.event.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yte.intern.project.user.entities.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long quota;

    private String eventName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;


//    @ElementCollection                    TODO: Sorular eklenmeli
//    private List<String> questions = null;

    @ManyToMany(mappedBy ="eventSet")
    private Set<User> userSet;

    public void AddUserToEvent(User user){
        userSet.add(user);
    }
}