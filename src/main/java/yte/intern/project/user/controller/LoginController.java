package yte.intern.project.user.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.user.controller.request.LoginRequest;
import yte.intern.project.user.service.LoginService;

import javax.servlet.http.HttpServletResponse;

@RestController
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/getname")
    public String getName(Authentication authentication){
        if(authentication!=null){
            return authentication.getName();
        }
        else{
            return "NO USER";
        }
    }

    @GetMapping("/logged")
    public boolean loggedIn(Authentication authentication){
        return authentication != null;
    }


    @PostMapping("/login")
    public MessageResponse login(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        return loginService.login(loginRequest,response);
    }

}
