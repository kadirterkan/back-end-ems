package yte.intern.project.user.configuration;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import yte.intern.project.common.services.JavaMailService;
import yte.intern.project.user.appevent.OnPasswordForgottenRequest;
import yte.intern.project.user.entities.BaseUser;
import yte.intern.project.user.service.UserService;

import java.util.Random;
import java.util.UUID;

@Component
public class ForgottenPasswordListener implements ApplicationListener<OnPasswordForgottenRequest> {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JavaMailService mailService;


    @Override
    public void onApplicationEvent(OnPasswordForgottenRequest request) {
        this.confirmRegistration(request);
    }

    private void confirmRegistration(OnPasswordForgottenRequest event){
        BaseUser baseUser = event.getBaseUser();
        Random random = new Random();
        String token = Long.toString((100000 + random.nextInt(900000)));
        userService.createVerificationToken(baseUser,token);

        String recipientAddress = baseUser.getEmail();
        String subject = "Password change request";
        String message = "THIS IS A TEST";
        mailService.passwordChangeRequest(recipientAddress,subject,message,token);
    }
}
