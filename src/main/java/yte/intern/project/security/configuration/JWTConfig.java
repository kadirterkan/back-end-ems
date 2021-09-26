package yte.intern.project.security.configuration;


import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import yte.intern.project.common.utils.JWTUtil;
import yte.intern.project.user.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;


@Component
public class JWTConfig extends OncePerRequestFilter {


    @Value("${security.jwt.secret-key}")
    private String SecretKey;

    private final UserService userService;

    @Autowired
    public JWTConfig(UserService userService){
        this.userService=userService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookie = request.getCookies();

        if (cookie!=null) {
            var authenticationCooke= Arrays.stream(cookie).filter((val)-> val.getName().equals("Authority")).findFirst();
            if(authenticationCooke.isPresent()){
                String authentication = authenticationCooke.get().getValue();
                if(authentication!=null) {
                    String username = JWTUtil.extractUsername(authentication,SecretKey);

                    UserDetails userDetails = userService.bringUserDetailsByUsername(username);

                    if(SecurityContextHolder.getContext().getAuthentication() == null){
                        var token = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(token);
                    }
                }
            }
        }
        filterChain.doFilter(request,response);
    }

}
