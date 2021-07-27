package yte.intern.project.user.loginjwt.configuration;


import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import yte.intern.project.user.loginjwt.jwt.JWTUtil;
import yte.intern.project.user.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@NoArgsConstructor
@Component
public class JWTConfig extends OncePerRequestFilter {


    @Value("SOMESECRETCODEABOUTTHISJAVASPRINGBOOTAPPLICATIONYOUMAYNOTSOLVETHISFORGODSSAKE")
    private String SecretKey;

    private UserService userService;

    public JWTConfig(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authenticationHeader = request.getHeader("Authorization");
        if(authenticationHeader!=null && authenticationHeader.startsWith("Bearer")) {
            String jwtToken = authenticationHeader.substring(7);
            String username = JWTUtil.extractUsername(jwtToken,SecretKey);
            UserDetails userDetails = userService.loadUserByUsername(username);

            if(SecurityContextHolder.getContext().getAuthentication() == null){
                var token = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);
            }
        }
        filterChain.doFilter(request,response);
    }

}
