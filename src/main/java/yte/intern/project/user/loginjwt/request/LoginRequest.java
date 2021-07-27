package yte.intern.project.user.loginjwt.request;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@ToString
public class LoginRequest {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}
