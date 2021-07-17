package yte.intern.project.user.enumer;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    public Set<SimpleGrantedAuthority> getSimpleGrantedAuthority(){
        Set<SimpleGrantedAuthority> grantedauthorities = getPermisions().stream()
                .map(permision -> new SimpleGrantedAuthority(permision.getPermision()))
                .collect(Collectors.toSet());
        grantedauthorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return grantedauthorities;
    }
}