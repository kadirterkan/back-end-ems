package yte.intern.project.user.entities;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import yte.intern.project.security.entities.VerificationToken;
import yte.intern.project.user.controller.request.IsThisYou;
import yte.intern.project.user.enums.RoleEnum;
import yte.intern.project.user.enums.Departments;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;


@Entity
@Accessors(fluent = true)
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class BaseUser implements UserDetails {


    public BaseUser(String username,
                    String firstName,
                    String lastName,
                    String email,
                    String password,
                    RoleEnum role,
                    Departments departments) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.departments = departments;
        this.enabled = false;
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

    @Lob
    @Column( length = 100000 )
    private String base64ProfilePicture;
    private Departments departments;
    private String jobDescription;

    @Column(name="enabled")
    private boolean enabled;

    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VerificationToken> verificationToken;

    public BaseUser() {
        this.enabled = false;
    }

    public String getBase64ProfilePicture() {
        return base64ProfilePicture;
    }

    public void setBase64ProfilePicture(String base64ProfilePicture) {
        this.base64ProfilePicture = base64ProfilePicture;
    }

    public Departments getDepartments() {
        return departments;
    }

    private RoleEnum role;

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

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setPassword(String newPassword){
        this.password = newPassword;
    }

    public IsThisYou toIsThisYou(){
        return new IsThisYou(this.role,this.username,this.base64ProfilePicture);
    }
}
