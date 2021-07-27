package yte.intern.project.user.loginjwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.user.loginjwt.jwt.JWTUtil;
import yte.intern.project.user.loginjwt.request.LoginRequest;

import static yte.intern.project.common.enums.MessageType.ERROR;
import static yte.intern.project.common.enums.MessageType.SUCCESS;

@Service
public class LoginService {

    @Value("SOMESECRETCODEABOUTTHISJAVASPRINGBOOTAPPLICATIONYOUMAYNOTSOLVETHISFORGODSSAKE")
    private String secretKey;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public LoginService(final AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public MessageResponse login(LoginRequest loginRequest){
        var token = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword());

        try{
            Authentication authenticationToken = authenticationManager.authenticate(token);
            String jwt = JWTUtil.generateToken(authenticationToken,secretKey);
            return new MessageResponse(SUCCESS,jwt);
        }catch(AuthenticationException exception){
            return new MessageResponse(ERROR, exception.getMessage());
        }
    }
}
