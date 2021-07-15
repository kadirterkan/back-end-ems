package yte.intern.project.user.enumer;

import java.util.HashSet;
import java.util.Set;

import static yte.intern.project.user.enumer.RolePermisionsEnum.*;

public enum RoleEnum {
    ADMIN(Set.of(EVENT_WRITE, EVENT_READ, EVENT_JOIN)),
    USER(Set.of(EVENT_READ,EVENT_JOIN));

    private final Set<RolePermisionsEnum> permisions;


    RoleEnum(Set<RolePermisionsEnum> permisions) {
        this.permisions = permisions;
    }

    public Set<RolePermisionsEnum> getPermisions() {
        return permisions;
    }
}