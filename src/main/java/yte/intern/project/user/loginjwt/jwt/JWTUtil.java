package yte.intern.project.user.loginjwt.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;


public class JWTUtil {

    public static String generateToken(Authentication user, String key){
        return Jwts.builder()
                .setSubject(user.getName())
                .claim("authorities", user.getAuthorities())
                .setExpiration(getExperationDate())
                .signWith(Keys.hmacShaKeyFor(key.getBytes()))
                .compact();
    }

    public static String extractUsername(String jwtToken, String secretKey){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
        return claims.getSubject();
    }

    private static List<String> getAuthorities(Authentication user){
        return user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }

    private static Date getExperationDate() {
        Instant instant = LocalDate.now()
                .plusWeeks(2)
                .atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant();
        return Date.from(instant);
    }
}
