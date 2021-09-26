package yte.intern.project.common.services;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import yte.intern.project.common.configuration.JavaMailConfiguration;
import yte.intern.project.event.entities.CustomEvent;
import yte.intern.project.user.entities.CustomUser;

import javax.mail.internet.MimeMessage;
import java.util.Objects;

@Service
public class JavaMailService {


    private final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    public JavaMailService(JavaMailConfiguration javaMailConfiguration) {

        mailSender.setHost(javaMailConfiguration.getHost());
        mailSender.setPort(javaMailConfiguration.getPort());
        mailSender.setUsername(javaMailConfiguration.getUsername());
        mailSender.setPassword(javaMailConfiguration.getPassword());

    }

    public void mailQrCodeSender(CustomUser customUser, CustomEvent customEvent,String base64QrCode) throws Exception {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message,true);


        helper.setFrom(Objects.requireNonNull(mailSender.getHost()));
        helper.setTo(customUser.getEmail());
        helper.setSubject("YOUR QRCODE FOR JOINING THE EVENT");
        helper.setText(
                "<html>" +"<body>" +"<div>Dear %s,".formatted(customUser.getLastName()) +
                        "<div> This is your QR Code for Event %s".formatted(customEvent.getEventName()) +
                        "</div>"+
                        "<div>" +
                        "<img src={%s}/>".formatted(base64QrCode) +
                        "</div>" +
                        "</div>" +
                        "</body>" +
                        "</html>",true
        );

        mailSender.send(message);
    }

    public void confirmRegistrationMailSender(String receiver,String subject,String message,String confirmationUrl){
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(receiver);
        email.setSubject(subject);
        email.setText(message + "\r\n" + confirmationUrl);
        mailSender.send(email);
    }


    public void passwordChangeRequest(String receiver,String subject,String message, String token){
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(receiver);
        email.setSubject(subject);
        email.setText(message + " " + token);
        mailSender.send(email);
    }
}
