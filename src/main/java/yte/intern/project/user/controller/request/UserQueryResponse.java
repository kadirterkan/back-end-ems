package yte.intern.project.user.controller.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import yte.intern.project.user.entities.CustomUser;

@Getter
public class UserQueryResponse {

    private final String firstName;

    private final String lastName;

    private final String tcKimlikNumber;

    public UserQueryResponse(final CustomUser customUser){
        this.firstName = customUser.getFirstName();
        this.lastName = customUser.getLastName();
        this.tcKimlikNumber = customUser.getTcKimlikNumber();
    }
}
