package yte.intern.project.common.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import yte.intern.project.event.entities.CustomEvent;
import yte.intern.project.user.entities.AppUser;
import yte.intern.project.user.entities.Authority;
import yte.intern.project.user.service.AuthorityService;
import yte.intern.project.user.service.UserService;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Component
public class AdminConstructer {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityService authorityService;

    @Autowired
    public AdminConstructer(UserService userService, PasswordEncoder passwordEncoder, AuthorityService authorityService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authorityService = authorityService;
    }


    @Transactional
    public void firstAdmin(){
        if(!userService.userExistswithUsername("ADMIN")){
            Set<Authority> authorities = authorityService.getAllAuthoritiesSet();
            AppUser admin = new AppUser("ADMIN",
                    "Kadir Tayyip",
                    "Erkan",
                    "12345678912",
                    "kadirtayyib@hotmail.com",
                    passwordEncoder.encode("root"),
                    authorities,
                    new HashSet<CustomEvent>());

            userService.AddUserToDb(admin);

            authorities.forEach((val)-> val.getAppUsers().add(admin));

            authorities.forEach(authorityService::updateAuthority);
        }

    }
}
