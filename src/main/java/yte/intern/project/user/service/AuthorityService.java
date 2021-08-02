package yte.intern.project.user.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yte.intern.project.user.entities.Authority;
import yte.intern.project.user.repository.AuthorityRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthorityService {

    private final AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    public Authority loadAuthorityByName(String authorityName) throws Exception {
        return authorityRepository.findByAuthority(authorityName)
                .orElseThrow(() -> new Exception("AUTHORITY NOT FOUND"));
    }

    public Authority updateAuthority(Authority newAuthority){
        return authorityRepository.save(newAuthority);
    }

    public void AdminAuthorityAdderToDb(){
        if(doesntExist("ADMIN")){
            Authority admin = new Authority(null,"ADMIN",new HashSet<>());
            authorityRepository.save(admin);
        }
        if(doesntExist("USER")){
            Authority user = new Authority(null,"USER",new HashSet<>());
            authorityRepository.save(user);
        }
        if(doesntExist("MODERATOR")){
            Authority moderator = new Authority(null,"MODERATOR",new HashSet<>());
            authorityRepository.save(moderator);
        }
    }

    private boolean doesntExist(String name){
        return !authorityRepository.existsByAuthority(name);
    }

    public Set<Authority> getAllAuthoritiesSet(){
        return new HashSet<>(authorityRepository.findAll());
    }

}
