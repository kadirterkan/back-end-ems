package yte.intern.project.user.entities;

import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import yte.intern.project.common.entities.UserEvent;
import yte.intern.project.event.entities.CustomEvent;
import yte.intern.project.user.enumer.RoleEnum;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
public class CustomUser extends BaseUser {

    public CustomUser(String username,
                      String firstName,
                      String lastName,
                      String email,
                      String password,
                      String tcKimlikNumber) {
        super(username,
                firstName,
                lastName,
                email,
                password,
                RoleEnum.USER);
        this.tcKimlikNumber = tcKimlikNumber;
    }

//    @ManyToMany
//    @JoinTable(
//            name = "USER_EVENT",
//            joinColumns = @JoinColumn(name = "USER_ID"),
//            inverseJoinColumns = @JoinColumn(name = "EVENT_ID")
//    )
//    private Set<CustomEvent> customEventSet;

    @OneToMany
    @MapsId("user")
    private Set<UserEvent> events;


    private String tcKimlikNumber;

    public CustomUser() {
        super(RoleEnum.USER);
    }


    public void AddEventToUser(UserEvent userEvent){
        events.add(userEvent);
    }

    public boolean leaveEvent(UserEvent userEvent){
        return events.remove(userEvent);
    }

    @Override
    public String toString() {
        return "CustomUser{" +
                "tcKimlikNumber='" + tcKimlikNumber + '\'' +
                '}';
    }
}
