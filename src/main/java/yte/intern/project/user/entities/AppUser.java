package yte.intern.project.user.entities;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.userdetails.UserDetails;
import yte.intern.project.event.entities.CustomEvent;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@NoArgsConstructor
@ToString
public class AppUser implements UserDetails {


    public AppUser(String username,
                   String firstName,
                   String lastName,
                   String tcKimlikNumber,
                   String email,
                   String password,
                   Set<Authority> authorities,
                   Set<CustomEvent> customEventSet) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.tcKimlikNumber = tcKimlikNumber;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.customEventSet = customEventSet;
    }

    public AppUser(String username,
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
        this.customEventSet = new HashSet<CustomEvent>();
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

    @ManyToMany(fetch = FetchType.EAGER)
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
    private Set<CustomEvent> customEventSet;

    @OneToMany(mappedBy = "createdBy")
    private Set<CustomEvent> createdEvents;


    public Collection<Authority> getAuthorities() {
        return authorities;
    }

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

    public void AddEventToUser(CustomEvent customEvent){
        customEventSet.add(customEvent);
    }

    public boolean addAuthorityToUser(Authority authority){
        return authorities.add(authority);
    }

    @PostConstruct
    private void Event(){
        this.createdEvents = new HashSet<CustomEvent>();
    }


}
