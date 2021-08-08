package yte.intern.project.user.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.common.entities.UserEvent;
import yte.intern.project.common.enums.MessageType;
import yte.intern.project.common.services.UserEventService;
import yte.intern.project.event.entities.CustomEvent;
import yte.intern.project.user.controller.request.ModeratorRequest;
import yte.intern.project.user.entities.CustomMod;
import yte.intern.project.user.entities.CustomUser;
import yte.intern.project.user.controller.request.SimpleUserRequest;
import yte.intern.project.user.repository.ModeratorRepository;
import yte.intern.project.user.repository.SimpleUserRepository;

import java.util.List;
import java.util.Optional;

import static yte.intern.project.common.enums.MessageType.SUCCESS;

@Service
@Transactional
public class UserService implements UserDetailsService {



    private static final String MOD_ALREADY_EXISTS = "MOD WITH USERNAME %s ALREADY EXISTS";
    private static final String USER_ALREADY_EXISTS = "USER WITH %s TC NO ALREADY EXISTS";
    private static final String USER_ADDED_SUCCESSFULLY = "USER WITH %s TC NO HAS BEEN SUCCESSFULLY ADDED";
    private static final String AUTHORITY_ADDED_SUCCESSFULLY = "USER WITH %s TC NO HAS RECEIVED THE %s AUTHORITY SUCCESSFULLY";
    private static final String EVENT_ADDED_SUCCESSFULLY = "EVENT WITH NAME %s HAS BEEN SUCCESSFULLY ADDED TO THE USER WITH %s TC NO";
    private static final String EVENT_DOESNT_EXIST = "EVENT WITH NAME %s DOESNT EXIST";
    private static final String USER_DOESNT_EXIST = "USER WITH %s TC NO DOESNT EXIST";
    private static final String MOD_ADDED_SUCCESSFULLY = "MOD WITH USERNAME %s SUCCESSFULLY ADDED";


    private final PasswordEncoder passwordEncoder;
    private final SimpleUserRepository simpleUserRepository;
    private final ModeratorRepository moderatorRepository;
    private final UserEventService userEventService;


    @Autowired
    public UserService(PasswordEncoder passwordEncoder,
                       SimpleUserRepository simpleUserRepository,
                       ModeratorRepository moderatorRepository, UserEventService userEventService) {
        this.passwordEncoder = passwordEncoder;
        this.simpleUserRepository = simpleUserRepository;
        this.moderatorRepository = moderatorRepository;
        this.userEventService = userEventService;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(simpleUserRepository.existsByUsername(username)){
            return simpleUserRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı Bulunamadı"));
        }else {
            return moderatorRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND"));
        }

    }

    public UserDetails loadUserByUserId(Long id) throws Exception {
        return simpleUserRepository.findById(id)
                .orElseThrow(()->new Exception("Kullanıcı Bulunamadı"));
    }

    public CustomUser bringUser(String username) throws UsernameNotFoundException{
        return simpleUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND"));
    }

    public CustomMod bringMod(String username) throws UsernameNotFoundException{
        return moderatorRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND"));
    }

    public void updateMod(CustomMod customMod){
        moderatorRepository.save(customMod);
    }

    public void updateUser(CustomUser customUser){
        simpleUserRepository.save(customUser);
    }


    public MessageResponse newSimpleUserRegistration(SimpleUserRequest simpleUserRequest) throws Exception{
        System.out.println(simpleUserRequest.toString());
        if(simpleUserRepository.existsByTcKimlikNumber(simpleUserRequest.getTcKimlikNumber())){
            System.out.println("EXISTS");
            return new MessageResponse(MessageType.ERROR,
                    USER_ALREADY_EXISTS.formatted(simpleUserRequest.getTcKimlikNumber()));
        }
        else{
            String username = simpleUserRequest.getFirstName()+"."+ simpleUserRequest.getLastName();
            CustomUser customUser = new CustomUser(username,
                    simpleUserRequest.getFirstName(),
                    simpleUserRequest.getLastName(),
                    simpleUserRequest.getEmail(),
                    passwordEncoder.encode(simpleUserRequest.getPassword()),
                    simpleUserRequest.getTcKimlikNumber()
            );


            simpleUserRepository.save(customUser);

            return new MessageResponse(SUCCESS,
                    USER_ADDED_SUCCESSFULLY.formatted(simpleUserRequest.getTcKimlikNumber()));
        }
    }

    public MessageResponse newModeratorRegistration(ModeratorRequest moderatorRequest) throws Exception{
        String username = moderatorRequest.getFirstName()+"."+moderatorRequest.getLastName()+"@"+moderatorRequest.getCompanyName();
        if(moderatorRepository.existsByUsername(username)){
            System.out.println("EXISTS");
            return new MessageResponse(MessageType.ERROR,
                    USER_ALREADY_EXISTS.formatted(username));
        }
        else{
            CustomMod customMod = new CustomMod(username,
                    moderatorRequest.getFirstName(),
                    moderatorRequest.getLastName(),
                    moderatorRequest.getEmail(),
                    passwordEncoder.encode(moderatorRequest.getPassword()),
                    moderatorRequest.getCompanyName(),
                    moderatorRequest.getDepartmentName());

            moderatorRepository.save(customMod);

            return new MessageResponse(SUCCESS,
                    MOD_ADDED_SUCCESSFULLY.formatted(username));
        }
    }


    public boolean userExistswithUsername(String username){
        return simpleUserRepository.existsByUsername(username);
    }

    public List<CustomUser> getAllUsers(){
        return simpleUserRepository.findAll();
    }


    public boolean joinedThisEvent(CustomUser customUser, CustomEvent customEvent){
        return userEventService.doesUserEventExists(customUser, customEvent);
    }


}