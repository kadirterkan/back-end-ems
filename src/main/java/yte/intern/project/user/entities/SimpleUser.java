package yte.intern.project.user.entities;

import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import yte.intern.project.event.entities.CustomEvent;
import yte.intern.project.user.enumer.RoleEnum;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.Collection;
import java.util.Set;

@Entity
@NoArgsConstructor
public class SimpleUser extends BaseUser {

    public SimpleUser(String username,
                      String firstName,
                      String lastName,
                      String tcKimlikNumber,
                      String email,
                      String password) {
        super(username,
                firstName,
                lastName,
                tcKimlikNumber,
                email,
                password);
    }

    @ManyToMany
    @JoinTable(
            name = "USER_EVENT",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "EVENT_ID")
    )
    private Set<CustomEvent> customEventSet;

    private final RoleEnum role = RoleEnum.USER;


    public void AddEventToUser(CustomEvent customEvent){
        customEventSet.add(customEvent);
    }

    public boolean leaveEvent(CustomEvent customEvent){
        return customEventSet.remove(customEvent);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return (Collection<? extends GrantedAuthority>) role.getGrantedAuthority();
    }
}
