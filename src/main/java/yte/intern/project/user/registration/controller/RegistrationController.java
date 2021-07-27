package yte.intern.project.user.registration.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.user.registration.request.RegisterRequest;
import yte.intern.project.user.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/registration")
@Validated
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public MessageResponse register(@Valid @RequestBody final RegisterRequest registerRequest) throws Exception {
        System.out.println(registerRequest);
        return userService.newUserRegistration(registerRequest);
    }
}
