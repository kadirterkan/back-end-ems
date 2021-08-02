package yte.intern.project.common.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import yte.intern.project.user.service.AuthorityService;
import yte.intern.project.user.service.UserService;

import javax.transaction.Transactional;

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
        }
    }
}
