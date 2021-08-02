package yte.intern.project.user.controller.request;


import lombok.RequiredArgsConstructor;

import lombok.ToString;
import yte.intern.project.event.entities.CustomEvent;
import yte.intern.project.user.entities.BaseUser;
import yte.intern.project.user.entities.Authority;
import yte.intern.project.user.entities.SimpleUser;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;

@RequiredArgsConstructor
@ToString
public class SimpleUserRequest {

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getTcKimlikNumber() {
        return tcKimlikNumber;
    }

    public String getEmail() {
        return email;
    }

    @NotEmpty(message = "İsim Kısmını Boş Bırakamazsınız")
    @Size(max = 255,message = "İsminiz Bu Kadar Uzun Olamaz")
    private final String firstName;

    @NotEmpty(message = "Soyisim Kısmını Boş Bırakamazsınız")
    @Size(max = 255, message = "Soyisminiz Bu Kadar Uzun Olamaz")
    private final String lastName;

    @NotEmpty(message = "Şifrenizi Lütfen Giriniz")
    @Size(max = 255)
    private final String password;

    @NotEmpty
    @Email
    @Size(max = 255)
    private final String email;

    @NotEmpty(message = "TC Kimlik Numaranızı Giriniz")
    @Size(max = 11,min = 11)
    private final String tcKimlikNumber;

    public SimpleUser toUser(){
        return new SimpleUser(firstName+"."+lastName,
                firstName,
                lastName,
                tcKimlikNumber,
                email,
                password);
    }

}
