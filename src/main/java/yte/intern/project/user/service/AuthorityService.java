package yte.intern.project.user.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import yte.intern.project.user.entities.AppUser;
import yte.intern.project.user.entities.Authority;
import yte.intern.project.user.repository.AuthorityRepository;
import yte.intern.project.user.repository.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthorityService {

    private final AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    public Authority loadAuthorityByName(String authorityName) throws Exception {
        return authorityRepository.findByName(authorityName)
                .orElseThrow(() -> new Exception("AUTHORITY NOT FOUND"));
    }

    public void AdminAuthorityAdderToDb(){
        if(doesntExist("ADMIN")){
            Authority admin = new Authority("ADMIN");
            authorityRepository.save(admin);
        }
        if(doesntExist("USER")){
            Authority user = new Authority("USER");
            authorityRepository.save(user);
        }
        if(doesntExist("MOD")){
            Authority moderator = new Authority("MOD");
            authorityRepository.save(moderator);
        }
    }

    private boolean doesntExist(String name){
        return !authorityRepository.existsByName(name);
    }

    public Set<Authority> getAllAuthoritiesSet(){
        return new HashSet<>(authorityRepository.findAll());
    }

}
