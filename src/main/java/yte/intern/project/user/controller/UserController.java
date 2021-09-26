package yte.intern.project.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.security.controller.request.UserInformationRequest;
import yte.intern.project.user.configuration.AuthenticationFacade;
import yte.intern.project.user.controller.request.GetEmail;
import yte.intern.project.user.controller.request.IsThisYou;
import yte.intern.project.user.controller.request.ModeratorRequest;
import yte.intern.project.user.controller.request.SimpleUserRequest;
import yte.intern.project.user.entities.BaseUser;
import yte.intern.project.user.entities.CustomUser;
import yte.intern.project.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
//@RequestMapping("/web-api/user-controller")
@Validated
public class UserController {

    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public UserController(UserService userService, AuthenticationFacade authenticationFacade) {
        this.userService = userService;
        this.authenticationFacade = authenticationFacade;
    }

    @PreAuthorize("hasAuthority('ROLE_MOD')")
    @GetMapping("/test")
    public String test(){
        return "THIS IS A TEST";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/list")
    public List<CustomUser> listUsers(){
        return userService.getAllUsers();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/bringuser")
    public UserDetails getUserByUsername(@RequestBody String username){
        return userService.loadUserByUsername(username);
    }

    @PostMapping("/user-registration")
    public MessageResponse registerSimpleUser(@Valid @RequestBody final SimpleUserRequest simpleUserRequest, HttpServletRequest request, Errors errors) throws Exception {
        return userService.newSimpleUserRegistration(simpleUserRequest,request,errors);
    }

    @PostMapping("/moderator-registration")
    public MessageResponse registerModerator(@Valid @RequestBody final ModeratorRequest moderatorRequest,HttpServletRequest request, Errors errors) throws Exception {
        return userService.newModeratorRegistration(moderatorRequest,request,errors);
    }

    @GetMapping("/registration-confirm/token/{token}")
    public MessageResponse confirmRegistration(WebRequest request, Model model, @PathVariable("token") String token){
        return userService.confirmRegistration(request,model,token);
    }

    @PostMapping("/forgotten-password/email")
    public IsThisYou getUserInfo(@RequestBody GetEmail response){
        BaseUser baseUser = userService.bringBaseUserByEmail(response.getThisguysemail());

        if(baseUser != null){
            return baseUser.toIsThisYou();
        }else {
            return null;
        }
    }

    @PostMapping("/forgotten-password/token-request/email")
    public MessageResponse sendTokenToMail(@RequestBody GetEmail email){
        return userService.sendForgottenPasswordToken(email.getThisguysemail());
    }

    @PostMapping("/forgotten-password/token/{token}")
    public MessageResponse confirmPasswordRequest(@RequestBody GetEmail email,@PathVariable("token") String token){
        return userService.confirmPasswordChange(token,email.getThisguysemail());
    }

    @PostMapping("/forgotten-password/password/{password}")
    public MessageResponse confirmPassword(@RequestBody GetEmail email,@PathVariable("password") String password){
        return userService.changePassword(email.getThisguysemail(),password);
    }

    @GetMapping("/user-type-and-name")
    public UserInformationRequest getUserInfo(){
        if (authenticationFacade.getAuthentication() != null) {
            String username = authenticationFacade.getAuthentication().getName();

            BaseUser customUser = userService.bringBaseUserByUsername(username);

            if (customUser !=null) {
                return new UserInformationRequest(username,customUser.getRole(), null);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}