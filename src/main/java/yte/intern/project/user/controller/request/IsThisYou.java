package yte.intern.project.user.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import yte.intern.project.user.enums.RoleEnum;


@Getter
@Setter
public class IsThisYou {

    private final RoleEnum roleEnum;

    private final String username;

    private final String base64Image;


    public IsThisYou(RoleEnum roleEnum, String username, String base64Image) {
        this.roleEnum = roleEnum;
        this.username = username;
        this.base64Image = base64Image;
    }
}
