package yte.intern.project.security.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yte.intern.project.user.enums.RoleEnum;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserInformationRequest {

    private String username;

    private RoleEnum roleEnum;

    private String base64Image;
}
