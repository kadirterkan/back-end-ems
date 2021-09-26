package yte.intern.project.security.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import yte.intern.project.common.services.JavaMailService;
import yte.intern.project.user.appevent.OnRegistrationCompleteEvent;
import yte.intern.project.user.entities.BaseUser;
import yte.intern.project.user.service.UserService;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JavaMailService mailService;


    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event){
        BaseUser baseUser = event.getBaseUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(baseUser,token);

        String recipientAddress = baseUser.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = "http://localhost:3000/registration-confirm/token/" + token;
        String message = "THIS IS A TEST";
        mailService.confirmRegistrationMailSender(recipientAddress,subject,message,confirmationUrl);

    }
}
