package yte.intern.project.user.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.context.request.WebRequest;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.common.enums.MessageType;
import yte.intern.project.security.controller.request.UserInformationRequest;
import yte.intern.project.user.appevent.OnPasswordForgottenRequest;
import yte.intern.project.user.appevent.OnRegistrationCompleteEvent;
import yte.intern.project.user.configuration.AuthenticationFacade;
import yte.intern.project.user.controller.request.ModeratorRequest;
import yte.intern.project.user.entities.BaseUser;
import yte.intern.project.user.entities.CustomMod;
import yte.intern.project.user.entities.CustomUser;
import yte.intern.project.user.controller.request.SimpleUserRequest;
import yte.intern.project.security.entities.VerificationToken;
import yte.intern.project.user.enums.RoleEnum;
import yte.intern.project.user.repository.BaseUserRepository;
import yte.intern.project.user.repository.ModeratorRepository;
import yte.intern.project.user.repository.CustomUserRepository;
import yte.intern.project.security.repository.VerificationTokenRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static yte.intern.project.common.enums.MessageType.ERROR;
import static yte.intern.project.common.enums.MessageType.SUCCESS;

@Service
@Transactional
public class UserService implements UserDetailsService {


    private static final String MOD_ALREADY_EXISTS = "MOD WITH USERNAME %s ALREADY EXISTS";
    private static final String USER_ALREADY_EXISTS = "USER WITH %s TC NO ALREADY EXISTS";
    private static final String USER_ADDED_SUCCESSFULLY = "USER WITH %s TC NO HAS BEEN SUCCESSFULLY ADDED";
    private static final String EVENT_ADDED_SUCCESSFULLY = "EVENT WITH NAME %s HAS BEEN SUCCESSFULLY ADDED TO THE USER WITH %s TC NO";
    private static final String EVENT_DOESNT_EXIST = "EVENT WITH NAME %s DOESNT EXIST";
    private static final String USER_DOESNT_EXIST = "USER WITH %s TC NO DOESNT EXIST";
    private static final String MOD_ADDED_SUCCESSFULLY = "MOD WITH USERNAME %s SUCCESSFULLY ADDED";


    private final PasswordEncoder passwordEncoder;
    private final BaseUserRepository baseUserRepository;
    private final CustomUserRepository customUserRepository;
    private final ModeratorRepository moderatorRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MessageSource messageSource;
    private final AuthenticationFacade authenticationFacade;


    @Autowired
    public UserService(PasswordEncoder passwordEncoder,
                       BaseUserRepository baseUserRepository,
                       CustomUserRepository customUserRepository,
                       ModeratorRepository moderatorRepository,
                       ApplicationEventPublisher applicationEventPublisher,
                       VerificationTokenRepository verificationTokenRepository,
                       MessageSource messageSource,
                       AuthenticationFacade authenticationFacade) {
        this.passwordEncoder = passwordEncoder;
        this.baseUserRepository = baseUserRepository;
        this.customUserRepository = customUserRepository;
        this.moderatorRepository = moderatorRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.verificationTokenRepository = verificationTokenRepository;
        this.messageSource = messageSource;
        this.authenticationFacade = authenticationFacade;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (customUserRepository.existsByUsername(username)) {
            return customUserRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND"));
        } else {
            return moderatorRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND"));
        }

    }

    public UserDetails loadUserByUserId(Long id) throws Exception {
        return customUserRepository.findById(id)
                .orElseThrow(() -> new Exception("USER NOT FOUND"));
    }

    public CustomUser bringUser(String username) throws UsernameNotFoundException {
        return customUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND"));
    }

    public CustomMod bringMod(String username) throws UsernameNotFoundException {
        return moderatorRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND"));
    }

    public void updateMod(CustomMod customMod) {
        moderatorRepository.save(customMod);
    }

    public void updateUser(CustomUser customUser) {
        customUserRepository.save(customUser);
    }


    public MessageResponse newSimpleUserRegistration(SimpleUserRequest simpleUserRequest, HttpServletRequest request, Errors errors) throws Exception {
        if (customUserRepository.existsByTcKimlikNumber(simpleUserRequest.getTcKimlikNumber())) {
            return new MessageResponse(MessageType.ERROR,
                    USER_ALREADY_EXISTS.formatted(simpleUserRequest.getTcKimlikNumber()));
        } else {
            String username = simpleUserRequest.getFirstName() + "." + simpleUserRequest.getLastName();
            CustomUser customUser = new CustomUser(username,
                    simpleUserRequest.getFirstName(),
                    simpleUserRequest.getLastName(),
                    simpleUserRequest.getEmail(),
                    passwordEncoder.encode(simpleUserRequest.getPassword()),
                    simpleUserRequest.getTcKimlikNumber(),
                    simpleUserRequest.getDepartment()
            );

            String appUrl = request.getContextPath();
            applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(appUrl,
                    request.getLocale(), customUser));

            customUserRepository.save(customUser);

            return new MessageResponse(SUCCESS,
                    USER_ADDED_SUCCESSFULLY.formatted(simpleUserRequest.getTcKimlikNumber()));
        }
    }

    public MessageResponse newModeratorRegistration(ModeratorRequest moderatorRequest, HttpServletRequest request, Errors errors) throws Exception {
        String username = moderatorRequest.getFirstName() + "." + moderatorRequest.getLastName() + "@" + moderatorRequest.getDepartment();
        if (moderatorRepository.existsByUsername(username)) {
            return new MessageResponse(MessageType.ERROR,
                    USER_ALREADY_EXISTS.formatted(username));
        } else {
            CustomMod customMod = new CustomMod(
                    username,
                    moderatorRequest.getFirstName(),
                    moderatorRequest.getLastName(),
                    moderatorRequest.getEmail(),
                    passwordEncoder.encode(moderatorRequest.getPassword()),
                    moderatorRequest.getDepartment());

            String appUrl = request.getContextPath();
            applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(appUrl,
                    request.getLocale(), customMod));

            moderatorRepository.save(customMod);

            return new MessageResponse(SUCCESS,
                    MOD_ADDED_SUCCESSFULLY.formatted(username));
        }
    }


    public boolean userExistswithUsername(String username) {
        return customUserRepository.existsByUsername(username);
    }

    public List<CustomUser> getAllUsers() {
        return customUserRepository.findAll();
    }


    public VerificationToken createVerificationToken(BaseUser baseUser, String token) {
        VerificationToken verificationToken = new VerificationToken(token, baseUser);
        return verificationTokenRepository.save(verificationToken);
    }

    public VerificationToken getVerifactionToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    public MessageResponse confirmRegistration(WebRequest request, Model model, String token) {
        VerificationToken verificationToken = getVerifactionToken(token);

        Locale locale = Locale.getDefault();

        if (verificationToken == null) {
//            String message = messageSource.getMessage("auth.message.invalidToken", null, locale);
//            model.addAttribute("message", message);
            return new MessageResponse(ERROR,"INVALID TOKEN");
        } else {

            Calendar calendar = Calendar.getInstance();
            if ((verificationToken.getExpiryDate().getTime() - calendar.getTime().getTime() <= 0)) {
                return new MessageResponse(ERROR,"INVALID TOKEN");
            } else {
                Long id = verificationToken.getUser().getId();
                RoleEnum role = verificationToken.getUser().getRole();
                if (role == RoleEnum.USER) {
                    Optional<CustomUser> customUser = customUserRepository.findById(id);
                    if (customUser.isPresent()) {
                        customUser.get().setEnabled(true);
                        customUserRepository.save(customUser.get());
                        return new MessageResponse(SUCCESS,"SUCCESSFUL VERIFICATION");
                    } else {
                        return new MessageResponse(ERROR,"INVALID USER");
                    }
                } else {
                    Optional<CustomMod> customMod = moderatorRepository.findById(id);
                    if (customMod.isPresent()) {
                        customMod.get().setEnabled(true);
                        moderatorRepository.save(customMod.get());
                        return new MessageResponse(SUCCESS,"SUCCESSFUL VERIFICATION");
                    } else {
                        return new MessageResponse(ERROR,"INVALID USER");
                    }
                }
            }
        }
    }

    public MessageResponse confirmPasswordChange(String token,String email){
        try{
            VerificationToken verificationToken = getVerifactionToken(token);

            if(verificationToken == null){
                throw new Exception("TOKEN IS INVALID");
            }else{
                Calendar calendar = Calendar.getInstance();
                if ((verificationToken.getExpiryDate().getTime() - calendar.getTime().getTime() <= 0)) {
                    throw new Exception("TOKEN HAS EXPIRED");
                } else{
                    if(!Objects.equals(verificationToken.getUser().getEmail(), email)){
                        throw new Exception("TOKEN IS INVALID");
                    }
                }
            }
        } catch (Exception e) {
            return new MessageResponse(ERROR,e.getMessage());
        }

        return new MessageResponse(SUCCESS,"TOKEN IS TRUE");
    }

    public UserDetails bringUserDetailsByUsername(String username){
        return baseUserRepository.findByUsername(username);
    }

    public BaseUser bringBaseUserByEmail(String email){
        return baseUserRepository.findByEmail(email);
    }

    public BaseUser bringBaseUserByUsername(String username){
        return baseUserRepository.getByUsername(username);
    }

    public boolean existsByEmail(String email){
        return baseUserRepository.existsByEmail(email);
    }

    public MessageResponse changePassword(String email,String password){

        System.out.println(email);
        try {
            if(customUserRepository.existsByEmail(email)){
                Optional<CustomUser> customUser = customUserRepository.findByEmail(email);
                System.out.println(customUser.get());
                if(customUser.isPresent()){

                    customUser.get().setPassword(passwordEncoder.encode(password));

                    customUserRepository.save(customUser.get());
                } else{
                    throw new Exception("USER DOESNT EXIST");
                }
            } else{
                throw new Exception("USER DOESNT EXIST");
            }
        } catch (Exception e) {
            return new MessageResponse(ERROR,e.getMessage());
        }

        return new MessageResponse(SUCCESS,"SUCCESSFULLY CHANGED PASSWORD");
    }

    public MessageResponse sendForgottenPasswordToken(String email){

        try {
            if(baseUserRepository.existsByEmail(email)){
                var baseUser = baseUserRepository.findByEmail(email);
                if(baseUser != null){
                    applicationEventPublisher.publishEvent(new OnPasswordForgottenRequest(baseUser));
                } else{
                    throw new Exception("USER DOESNT EXIST");
                }
            } else{
                throw new Exception("USER DOESNT EXIST");
            }
        } catch (Exception e) {
            return new MessageResponse(ERROR,e.getMessage());
        }

        return new MessageResponse(SUCCESS,"SUCCESSFULLY SENT EMAIL");
    }

    public UserInformationRequest getUserInfo(){
        String username = authenticationFacade.getAuthentication().getName();
        BaseUser customUser = baseUserRepository.getByUsername(username);

        return new UserInformationRequest(username,customUser.getRole(),customUser.getBase64ProfilePicture());
    }
}