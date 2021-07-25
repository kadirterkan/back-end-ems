package yte.intern.project.user.loginjwt.request;

import javax.validation.constraints.NotEmpty;

public class LoginRequest {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}
