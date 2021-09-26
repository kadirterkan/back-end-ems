package yte.intern.project.user.controller.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import yte.intern.project.user.entities.CustomMod;
import yte.intern.project.user.enums.Departments;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@RequiredArgsConstructor
@ToString
@Getter
public class ModeratorRequest {


    @NotEmpty
    @Size(max = 255)
    private final String firstName;

    @NotEmpty
    @Size(max = 255)
    private final String lastName;

    @NotEmpty
    @Size(max = 255)
    private final String password;

    @NotEmpty
    @Email
    @Size(max = 255)
    private final String email;


    private final Departments department;

    public CustomMod toModerator(){
        return new CustomMod(
                this.firstName+"."+this.lastName+"."+this.department,
                this.firstName,
                this.lastName,
                this.email,
                this.password,
                this.department
        );
    }
}
