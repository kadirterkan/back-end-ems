package yte.intern.project.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.user.controller.request.UpdateAuthorityRequest;
import yte.intern.project.user.controller.request.SimpleUserRequest;
import yte.intern.project.user.entities.SimpleUser;
import yte.intern.project.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/test")
    public String test(){
        return "THIS IS A TEST";
    }
//
//    @PreAuthorize("hasAuthority('ADMIN')")
//    @PostMapping("/addauthority")
//    public MessageResponse addAuthorityToUser(@RequestBody UpdateAuthorityRequest updateAuthorityRequest) throws Exception {
//        return userService.addAuthorityToUser(updateAuthorityRequest.getUsername(),
//                updateAuthorityRequest.getAuthorityName());
//    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/list")
    public List<SimpleUser> listUsers(){
        return userService.getAllUsers();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/bringuser")
    public UserDetails getUserByUsername(@RequestBody String username){
        return userService.loadUserByUsername(username);
    }

    @PostMapping("/registration")
    public MessageResponse register(@Valid @RequestBody final SimpleUserRequest simpleUserRequest) throws Exception {
        System.out.println(simpleUserRequest);
        return userService.newSimpleUserRegistration(simpleUserRequest);
    }
}