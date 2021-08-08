package yte.intern.project.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.event.entities.CustomEvent;
import yte.intern.project.user.controller.request.ModeratorRequest;
import yte.intern.project.user.controller.request.SimpleUserRequest;
import yte.intern.project.user.entities.CustomUser;
import yte.intern.project.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("webapi/user")
@Validated
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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


    @PostMapping("/userreg")
    public MessageResponse registerSimpleUser(@Valid @RequestBody final SimpleUserRequest simpleUserRequest) throws Exception {
        System.out.println(simpleUserRequest);
        return userService.newSimpleUserRegistration(simpleUserRequest);
    }

    @PostMapping("/modreg")
    public MessageResponse registerModerator(@Valid @RequestBody final ModeratorRequest moderatorRequest) throws Exception {
        return userService.newModeratorRegistration(moderatorRequest);
    }

}