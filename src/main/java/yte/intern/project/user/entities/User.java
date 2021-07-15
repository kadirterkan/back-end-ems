package yte.intern.project.user.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import yte.intern.project.event.entities.Event;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails {

    public User(String username,
                String firstName,
                String lastName,
                String tcKimlikNumber,
                String email,
                String password,
                Set<Authority> authorities,
                Set<Event> eventSet) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.tcKimlikNumber = tcKimlikNumber;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.eventSet = eventSet;
    }

    public User(String username,
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

    @ManyToMany
    @JoinTable(
            name = "USER_AUTHORITIES",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "AUTHORITY_ID")
    )
    private Set<Authority> authorities;


    @ManyToMany
    @JoinTable(
            name = "USER_EVENT",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "EVENT_ID")
    )
    private Set<Event> eventSet;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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

    public void AddEventToUser(Event event){
        eventSet.add(event);
    }

    public void AddAuthorityToUser(Authority authority){
        authorities.add(authority);
    }
}
