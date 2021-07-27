package yte.intern.project.user.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Authority(String name) {
        this.name = name;
    }

    @ManyToMany(mappedBy = "authorities")
    private Set<AppUser> appUsers;

    @Override
    public String getAuthority() {
        return name;
    }
}