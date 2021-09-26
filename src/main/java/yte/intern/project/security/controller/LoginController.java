package yte.intern.project.security.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.security.controller.request.LoginRequest;
import yte.intern.project.security.controller.request.UserInformationRequest;
import yte.intern.project.security.service.LoginService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/get-name")
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


    @PostMapping("/user-login")
    public MessageResponse userLogin(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        return loginService.userLogin(loginRequest,response);
    }

    @PostMapping("/mod-login")
    public MessageResponse modLogin(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        return loginService.modLogin(loginRequest,response);
    }

    @RequestMapping(value="/log-out", method = RequestMethod.GET)
    public String customLogout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        for (Cookie cookie : request.getCookies()) {
            String cookieName = cookie.getName();
            Cookie cookieToDelete = new Cookie(cookieName, null);
            cookieToDelete.setMaxAge(0);
            response.addCookie(cookieToDelete);
        }

        System.out.println(authentication.toString());
        if (authentication != null){
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            request.getCookies();// <= This is the call you are looking for.
        }
        return "redirect:/";
    }
}
