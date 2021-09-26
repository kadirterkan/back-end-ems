package yte.intern.project.user.entities;

import yte.intern.project.event.entities.CustomEvent;
import yte.intern.project.user.enums.RoleEnum;
import yte.intern.project.user.enums.Departments;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class CustomMod extends BaseUser{

    @OneToMany(mappedBy = "createdBy")
    private Set<CustomEvent> createdEvents;

    public CustomMod(String username,
                     String firstName,
                     String lastName,
                     String email,
                     String password,
                     Departments departments) {
        super(username,
                firstName,
                lastName,
                email,
                password,
                RoleEnum.MOD,
                departments);
        this.createdEvents = new HashSet<>();
    }

    public CustomMod() {
        super(RoleEnum.MOD);
    }

    public boolean addCreatedEvent(CustomEvent createdEvent){
        return createdEvents.add(createdEvent);
    }


}
