package yte.intern.project.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.common.enums.MessageType;
import yte.intern.project.common.utils.JWTUtil;
import yte.intern.project.security.controller.request.LoginRequest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static yte.intern.project.common.enums.MessageType.ERROR;
import static yte.intern.project.common.enums.MessageType.SUCCESS;

@Service
public class LoginService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    private static final String SUCCESSFULLY_LOGGED = "YOU HAVE SUCCESSFULLY LOGGED IN";

    private final AuthenticationManager authenticationManager;

    @Autowired
    public LoginService(final AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public MessageResponse userLogin(LoginRequest loginRequest, HttpServletResponse response){

        var token = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword());


        try{
            Authentication authenticationToken = authenticationManager.authenticate(token);

            if(!authenticationToken.getAuthorities().toString().equals("[ROLE_USER]")){
                throw new Exception("YOU ARE NOT A STANDARD USER");
            }

            String jwt = JWTUtil.generateToken(authenticationToken,secretKey);

            Cookie jwt_cookie = new Cookie("Authority",jwt);
            response.addCookie(jwt_cookie);

            System.out.println(jwt);

            return new MessageResponse(SUCCESS,SUCCESSFULLY_LOGGED);
        } catch(Exception exception){
            return new MessageResponse(ERROR, exception.getMessage());
        }
    }

    public MessageResponse modLogin(LoginRequest loginRequest, HttpServletResponse response){

        var token = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword());

        try{
            Authentication authenticationToken = authenticationManager.authenticate(token);

            if(!authenticationToken.getAuthorities().toString().equals("[ROLE_MOD]")){
                throw new Exception("YOU ARE NOT A MODERATOR");
            }

            String jwt = JWTUtil.generateToken(authenticationToken,secretKey);

            Cookie jwt_cookie = new Cookie("Authority",jwt);
            response.addCookie(jwt_cookie);

            return new MessageResponse(MessageType.SUCCESS,SUCCESSFULLY_LOGGED);
        } catch(Exception exception){
            return new MessageResponse(ERROR, exception.getMessage());
        }
    }
}
