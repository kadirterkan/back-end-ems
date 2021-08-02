package yte.intern.project.user.loginjwt.configuration;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
    Authentication getAuthentication();
}
