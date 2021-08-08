package yte.intern.project.user.service;

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
import yte.intern.project.user.controller.request.LoginRequest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static yte.intern.project.common.enums.MessageType.ERROR;
import static yte.intern.project.common.enums.MessageType.SUCCESS;

@Service
public class LoginService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    private static final String SUCCESSFULLY_LOGGED = "YOU HAVE SUCCESSFULLY LOGGED IN";
    private static final String SUCCESSFULLY_LOGGED_MOD = "WELCOME DEAR %s";

    private final AuthenticationManager authenticationManager;

    @Autowired
    public LoginService(final AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public MessageResponse login(LoginRequest loginRequest, HttpServletResponse response){

        var token = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword());

        try{
            Authentication authenticationToken = authenticationManager.authenticate(token);
            String jwt = JWTUtil.generateToken(authenticationToken,secretKey);

            Cookie jwt_cookie = new Cookie("Authority",jwt);
            response.addCookie(jwt_cookie);

            String authorityName = authenticationToken.getAuthorities().toString();


            if(authorityName.equals("[ROLE_USER]")){
                return new MessageResponse(MessageType.LOGIN_USER,SUCCESSFULLY_LOGGED);
            }
            else if(authorityName.equals("[ROLE_MOD]")){
                return new MessageResponse(MessageType.LOGIN_MOD,
                        SUCCESSFULLY_LOGGED_MOD.formatted(loginRequest.getUsername()));
            }
            return new MessageResponse(SUCCESS,SUCCESSFULLY_LOGGED);
        }catch(AuthenticationException exception){
            return new MessageResponse(ERROR, exception.getMessage());
        }
    }
}
