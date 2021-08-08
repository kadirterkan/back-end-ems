package yte.intern.project.user.entities;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import yte.intern.project.user.enumer.RoleEnum;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;


@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Accessors(fluent = true)
public abstract class BaseUser implements UserDetails {


    public BaseUser(String username,
                    String firstName,
                    String lastName,
                    String email,
                    String password,
                    RoleEnum role) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public BaseUser(RoleEnum role) {
        this.role = role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private final RoleEnum role;

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public Long getId() {
        return Id;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getFirstName() {
        return firstName;
    }

    public RoleEnum getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getLastName() {
        return lastName;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(this.role.getAuthority());
    }
}
