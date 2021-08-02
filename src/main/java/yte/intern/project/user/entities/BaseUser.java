package yte.intern.project.user.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.userdetails.UserDetails;
import yte.intern.project.event.entities.CustomEvent;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Accessors(fluent = true)
@NoArgsConstructor
public abstract class BaseUser implements UserDetails {


    public BaseUser(String username,
                    String firstName,
                    String lastName,
                    String tcKimlikNumber,
                    String email,
                    String password) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.tcKimlikNumber = tcKimlikNumber;
        this.email = email;
        this.password = password;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String username;
    private String firstName;
    private String lastName;
    private String tcKimlikNumber;
    private String email;
    private String password;

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

    public String getTcKimlikNumber() {
        return tcKimlikNumber;
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


    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", tcKimlikNumber='" + tcKimlikNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
