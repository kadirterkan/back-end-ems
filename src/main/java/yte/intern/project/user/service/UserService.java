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
import yte.intern.project.user.entities.AppUser;
import yte.intern.project.user.entities.Authority;
import yte.intern.project.user.registration.request.RegisterRequest;
import yte.intern.project.user.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    private final UserRepository userRepository;
    private final AuthorityService authorityService;


    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, AuthorityService authorityService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authorityService = authorityService;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı Bulunamadı"));
    }

    public UserDetails loadUserByUserId(Long id) throws Exception {
        return userRepository.findById(id)
                .orElseThrow(()->new Exception("Kullanıcı Bulunamadı"));
    }

    public AppUser bringUser(String username) throws UsernameNotFoundException{
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND"));
    }

    public void updateUser(AppUser appUser){
        userRepository.save(appUser);
    }


    public MessageResponse newUserRegistration(RegisterRequest registerRequest) throws Exception{
        if(userRepository.existsByTcKimlikNumber(registerRequest.getTcKimlikNumber())){
            System.out.println("EXISTS");
            return new MessageResponse(MessageType.ERROR,
                    USER_ALREADY_EXISTS.formatted(registerRequest.getTcKimlikNumber()));
        }
        else{

            AppUser appUser = new AppUser();
            appUser.setEmail(registerRequest.getEmail());
            appUser.setUsername(registerRequest.getFirstName()+"."+registerRequest.getLastName());
            appUser.setFirstName(registerRequest.getFirstName());
            appUser.setLastName(registerRequest.getLastName());
            appUser.setTcKimlikNumber(registerRequest.getTcKimlikNumber());
            appUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            appUser.setAuthorities(new HashSet<Authority>(Set.of(authorityService.loadAuthorityByName("USER"))));

            System.out.println(appUser);

            userRepository.save(appUser);

            return new MessageResponse(SUCCESS,
                    USER_ADDED_SUCCESSFULLY.formatted(registerRequest.getTcKimlikNumber()));
        }
    }

    public MessageResponse addAuthorityToUser(String userName, String authorityName) throws Exception {
        Optional<AppUser> appUser =  userRepository.findByUsername(userName);
        if(appUser.isPresent()){
            Authority authority = authorityService.loadAuthorityByName(authorityName);
//            AppUser oldRef = userRepository.getById(appUser.get().getId());
//            oldRef.addAuthorityToUser(authority);
            appUser.get().addAuthorityToUser(authority);
            userRepository.save(appUser.get());
            return new MessageResponse(SUCCESS,
                    AUTHORITY_ADDED_SUCCESSFULLY.formatted(appUser.get().getTcKimlikNumber(),
                            authorityName));
        }
        else{
            return new MessageResponse(MessageType.ERROR,
                    USER_DOESNT_EXIST.formatted(userName));
        }
    }


    public boolean userExistswithUsername(String username){
        return userRepository.existsByUsername(username);
    }

    public List<AppUser> getAllUsers(){
        return userRepository.findAll();
    }

    @Transactional
    public MessageResponse AddUserToDb(AppUser appUser){
        if(!userRepository.existsByUsername(appUser.getUsername())){
            userRepository.save(appUser);
            return new MessageResponse(SUCCESS,
                    USER_ADDED_SUCCESSFULLY.formatted(appUser.getTcKimlikNumber()));
        }
        else{
            return new MessageResponse(ERROR,
                    USER_ALREADY_EXISTS.formatted(appUser.getTcKimlikNumber()));
        }
    }
}