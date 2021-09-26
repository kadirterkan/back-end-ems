package yte.intern.project.user.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum RoleEnum {
    ADMIN,
    MOD,
    USER;


    public SimpleGrantedAuthority getAuthority(){
        return new SimpleGrantedAuthority("ROLE_" + this.name());
    }
}