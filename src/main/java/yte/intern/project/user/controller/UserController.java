package yte.intern.project.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.user.controller.request.UpdateAuthorityRequest;
import yte.intern.project.user.entities.AppUser;
import yte.intern.project.user.service.UserService;

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

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/addauthority")
    public MessageResponse addAuthorityToUser(@RequestBody UpdateAuthorityRequest updateAuthorityRequest) throws Exception {
        return userService.addAuthorityToUser(updateAuthorityRequest.getUsername(),
                updateAuthorityRequest.getAuthorityName());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/list")
    public List<AppUser> listUsers(){
        return userService.getAllUsers();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/bringuser")
    public UserDetails getUserByUsername(@RequestBody String username){
        return userService.loadUserByUsername(username);
    }
}