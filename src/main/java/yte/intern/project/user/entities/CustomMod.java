package yte.intern.project.user.entities;

import lombok.Getter;
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
public class CustomMod extends BaseUser{

    @OneToMany(mappedBy = "createdBy")
    private Set<CustomEvent> createdEvents;

    private String companyName;
    private String departmentName;

    public CustomMod(String username,
                     String firstName,
                     String lastName,
                     String email,
                     String password,
                     String companyName,
                     String departmentName) {
        super(username,
                firstName,
                lastName,
                email,
                password,
                RoleEnum.MOD);
        this.companyName = companyName;
        this.createdEvents = new HashSet<>();
        this.departmentName = departmentName;
    }

    public CustomMod() {
        super(RoleEnum.MOD);
    }

    public boolean addCreatedEvent(CustomEvent createdEvent){
        return createdEvents.add(createdEvent);
    }


}
