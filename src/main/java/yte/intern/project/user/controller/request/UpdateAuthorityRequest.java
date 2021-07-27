package yte.intern.project.user.controller.request;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@RequiredArgsConstructor
public class UpdateAuthorityRequest {

    @NotEmpty
    @Size(max = 255)
    private final String username;

    @NotEmpty
    @Size(max = 255)
    private final String authorityName;
}
