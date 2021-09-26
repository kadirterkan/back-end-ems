package yte.intern.project.user.controller.request;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import lombok.ToString;
import yte.intern.project.user.entities.CustomUser;
import yte.intern.project.user.enums.Departments;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@RequiredArgsConstructor
@ToString
@Getter
public class SimpleUserRequest {

    @Size(max = 255)
    private final String firstName;

    @Size(max = 255)
    private final String lastName;

    @Size(max = 255)
    private final String password;

    @NotEmpty
    @Email
    @Size(max = 255)
    private final String email;

    @Size(max = 11,min = 11)
    private final String tcKimlikNumber;

    private final Departments department;

    public CustomUser toUser(){
        return new CustomUser(firstName+"."+lastName,
                firstName,
                lastName,
                email,
                password,
                tcKimlikNumber,
                department);
    }


}
