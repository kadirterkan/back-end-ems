package yte.intern.project.user.appevent;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import yte.intern.project.user.entities.BaseUser;

import javax.persistence.GeneratedValue;
import java.util.Locale;

@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private String appUrl;
    private Locale locale;
    private BaseUser baseUser;


    public OnRegistrationCompleteEvent(String appUrl, Locale locale, BaseUser baseUser) {
        super(baseUser);
        this.appUrl = appUrl;
        this.locale = locale;
        this.baseUser = baseUser;
    }
}
