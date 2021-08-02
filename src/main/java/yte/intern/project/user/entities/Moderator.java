package yte.intern.project.user.entities;

import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import yte.intern.project.event.entities.CustomEvent;
import yte.intern.project.user.enumer.RoleEnum;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
public class Moderator extends BaseUser{

    @OneToMany(mappedBy = "createdBy")
    private Set<CustomEvent> createdEvents;

    private String companyName;

    private final RoleEnum role = RoleEnum.MODERATOR;

    public Moderator(String username,
                     String firstName,
                     String lastName,
                     String tcKimlikNumber,
                     String email,
                     String password,
                     Set<CustomEvent> createdEvents,
                     String companyName) {
        super(username,
                firstName,
                lastName,
                tcKimlikNumber,
                email,
                password);
        this.createdEvents = createdEvents;
        this.companyName = companyName;
        this.createdEvents = new HashSet<CustomEvent>();
    }

    public boolean addCreatedEvent(CustomEvent createdEvent){
        return createdEvents.add(createdEvent);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return (Collection<? extends GrantedAuthority>) role.getGrantedAuthority();
    }
}
