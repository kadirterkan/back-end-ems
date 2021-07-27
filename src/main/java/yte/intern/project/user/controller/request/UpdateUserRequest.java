package yte.intern.project.user.controller.request;


import lombok.RequiredArgsConstructor;
import yte.intern.project.user.entities.AppUser;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@RequiredArgsConstructor
public class UpdateUserRequest {

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

    public AppUser toUser(){
        return new AppUser(firstName+"."+lastName,
                firstName,
                lastName,
                tcKimlikNumber,
                email,
                password);
    }
}
