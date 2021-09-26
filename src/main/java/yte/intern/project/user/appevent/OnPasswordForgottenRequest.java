package yte.intern.project.user.appevent;


import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import yte.intern.project.user.entities.BaseUser;

@Getter
@Setter
public class OnPasswordForgottenRequest extends ApplicationEvent {

    private BaseUser baseUser;

    public OnPasswordForgottenRequest(BaseUser baseUser) {
        super(baseUser);
        this.baseUser = baseUser;
    }
}
