package yte.intern.project.user.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setTcKimlikNumber(String tcKimlikNumber) {
        this.tcKimlikNumber = tcKimlikNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public void setEventSet(Set<Event> eventSet) {
        this.eventSet = eventSet;
    }

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "USER_AUTHORITIES",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "AUTH_ID")
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

}
