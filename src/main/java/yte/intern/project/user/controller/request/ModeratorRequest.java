package yte.intern.project.user.controller.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import yte.intern.project.user.entities.CustomMod;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@RequiredArgsConstructor
@ToString
public class ModeratorRequest {

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getDepartmentName() {
        return departmentName;
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


    @NotEmpty
    @Size(max=255)
    private final String companyName;

    @NotEmpty
    @Size(max=255)
    private final String departmentName;

    public CustomMod toModerator(){
        return new CustomMod(
                this.firstName+"."+this.lastName+"."+this.companyName,
                this.firstName,
                this.lastName,
                this.email,
                this.password,
                this.companyName,
                this.departmentName
        );
    }
}
