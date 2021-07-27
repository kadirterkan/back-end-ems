package yte.intern.project.user.loginjwt.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.user.loginjwt.request.LoginRequest;
import yte.intern.project.user.loginjwt.service.LoginService;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public MessageResponse login(@RequestBody LoginRequest loginRequest){
        System.out.println(loginRequest);
        return loginService.login(loginRequest);
    }


}
