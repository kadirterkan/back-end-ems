package yte.intern.project.user.entities;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Authority implements GrantedAuthority {

    @Override
    public String getAuthority() {
        return authority;
    }

    public Set<AppUser> getAppUsers() {
        return appUsers;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authority;


    @ManyToMany(fetch = FetchType.EAGER,mappedBy = "authorities")
    private Set<AppUser> appUsers;
}