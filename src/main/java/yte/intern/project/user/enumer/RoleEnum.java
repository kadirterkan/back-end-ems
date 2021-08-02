package yte.intern.project.user.enumer;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static yte.intern.project.user.enumer.RolePermisionsEnum.*;

public enum RoleEnum {
    ADMIN,
    MODERATOR,
    USER;


    public GrantedAuthority getGrantedAuthority(){
        return new SimpleGrantedAuthority("ROLE_" + this.name());
    }
}