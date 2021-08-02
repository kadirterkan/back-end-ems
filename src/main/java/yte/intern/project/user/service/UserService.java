package yte.intern.project.user.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.common.enums.MessageType;
import yte.intern.project.event.entities.CustomEvent;
import yte.intern.project.user.entities.SimpleUser;
import yte.intern.project.user.entities.Authority;
import yte.intern.project.user.controller.request.SimpleUserRequest;
import yte.intern.project.user.repository.ModeratorRepository;
import yte.intern.project.user.repository.SimpleUserRepository;

import java.util.List;
import java.util.Optional;

import static yte.intern.project.common.enums.MessageType.ERROR;
import static yte.intern.project.common.enums.MessageType.SUCCESS;

@Service
@Transactional
public class UserService implements UserDetailsService {



    private static final String USER_ALREADY_EXISTS = "USER WITH %s TC NO ALREADY EXISTS";
    private static final String USER_ADDED_SUCCESSFULLY = "USER WITH %s TC NO HAS BEEN SUCCESSFULLY ADDED";
    private static final String AUTHORITY_ADDED_SUCCESSFULLY = "USER WITH %s TC NO HAS RECEIVED THE %s AUTHORITY SUCCESSFULLY";
    private static final String EVENT_ADDED_SUCCESSFULLY = "EVENT WITH NAME %s HAS BEEN SUCCESSFULLY ADDED TO THE USER WITH %s TC NO";
    private static final String EVENT_DOESNT_EXIST = "EVENT WITH NAME %s DOESNT EXIST";
    private static final String USER_DOESNT_EXIST = "USER WITH %s TC NO DOESNT EXIST";


    private final PasswordEncoder passwordEncoder;
    private final SimpleUserRepository simpleUserRepository;
    private final ModeratorRepository moderatorRepository;
    private final AuthorityService authorityService;


    @Autowired
    public UserService(PasswordEncoder passwordEncoder, SimpleUserRepository simpleUserRepository, ModeratorRepository moderatorRepository, AuthorityService authorityService) {
        this.passwordEncoder = passwordEncoder;
        this.simpleUserRepository = simpleUserRepository;
        this.moderatorRepository = moderatorRepository;
        this.authorityService = authorityService;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return simpleUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı Bulunamadı"));
    }

    public UserDetails loadUserByUserId(Long id) throws Exception {
        return simpleUserRepository.findById(id)
                .orElseThrow(()->new Exception("Kullanıcı Bulunamadı"));
    }

    public SimpleUser bringUser(String username) throws UsernameNotFoundException{
        return simpleUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND"));
    }

    public void updateUser(SimpleUser simpleUser){
        simpleUserRepository.save(simpleUser);
    }


    public MessageResponse newSimpleUserRegistration(SimpleUserRequest simpleUserRequest) throws Exception{
        if(simpleUserRepository.existsByTcKimlikNumber(simpleUserRequest.getTcKimlikNumber())){
            System.out.println("EXISTS");
            return new MessageResponse(MessageType.ERROR,
                    USER_ALREADY_EXISTS.formatted(simpleUserRequest.getTcKimlikNumber()));
        }
        else{
            String username = simpleUserRequest.getFirstName()+"."+ simpleUserRequest.getLastName();
            SimpleUser simpleUser = new SimpleUser(username,
                    simpleUserRequest.getFirstName(),
                    simpleUserRequest.getLastName(),
                    simpleUserRequest.getTcKimlikNumber(),
                    simpleUserRequest.getEmail(),
                    passwordEncoder.encode(simpleUserRequest.getPassword()));


            simpleUserRepository.save(simpleUser);

            return new MessageResponse(SUCCESS,
                    USER_ADDED_SUCCESSFULLY.formatted(simpleUserRequest.getTcKimlikNumber()));
        }
    }

    public MessageResponse addAuthorityToUser(String userName, String authorityName) throws Exception {
        Optional<SimpleUser> simpleAppUser =  simpleUserRepository.findByUsername(userName);
        if(simpleAppUser.isPresent()){
            Authority authority = authorityService.loadAuthorityByName(authorityName);
//            AppUser oldRef = userRepository.getById(simpleAppUser.get().getId());
//            oldRef.addAuthorityToUser(authority);
//            simpleAppUser.get().addAuthorityToUser(authority);
            simpleUserRepository.save(simpleAppUser.get());
            return new MessageResponse(SUCCESS,
                    AUTHORITY_ADDED_SUCCESSFULLY.formatted(simpleAppUser.get().getTcKimlikNumber(),
                            authorityName));
        }
        else{
            return new MessageResponse(MessageType.ERROR,
                    USER_DOESNT_EXIST.formatted(userName));
        }
    }


    public boolean userExistswithUsername(String username){
        return simpleUserRepository.existsByUsername(username);
    }

    public List<SimpleUser> getAllUsers(){
        return simpleUserRepository.findAll();
    }

    @Transactional
    public MessageResponse AddUserToDb(SimpleUser simpleUser){
        if(!simpleUserRepository.existsByUsername(simpleUser.getUsername())){
            simpleUserRepository.save(simpleUser);
            return new MessageResponse(SUCCESS,
                    USER_ADDED_SUCCESSFULLY.formatted(simpleUser.getTcKimlikNumber()));
        }
        else{
            return new MessageResponse(ERROR,
                    USER_ALREADY_EXISTS.formatted(simpleUser.getTcKimlikNumber()));
        }
    }

    public boolean joinedThisEvent(SimpleUser simpleUser, CustomEvent customEvent){
        return !simpleUserRepository.existsAppUserByCustomEventSetContains(customEvent);
    }
}