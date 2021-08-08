package yte.intern.project.common.services;


import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import yte.intern.project.common.configuration.JavaMailConfiguration;
import yte.intern.project.common.utils.QRcodeUtil;
import yte.intern.project.event.controller.request.LittleEventRequest;
import yte.intern.project.event.entities.CustomEvent;
import yte.intern.project.event.service.CustomEventService;
import yte.intern.project.user.entities.CustomUser;

import javax.imageio.ImageIO;
import javax.mail.internet.MimeMessage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
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

    public void mailSender(CustomUser customUser, CustomEvent customEvent, LocalDateTime joinTime) throws Exception {

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
                        "<img src='cid:qrcodeimage'/>" +
                        "</div>" +
                        "</div>" +
                        "</body>" +
                        "</html>",true
        );


        String context = customUser.toString() + '\n' + customEvent.toString() + '\n' + "joinTime=" +joinTime.toString();

        BufferedImage qrCode = QRcodeUtil.generateQRCodeImage(context);

        File outputfile = new File("image.jpg");

        ImageIO.write(qrCode, "jpg", outputfile);

        helper.addInline("qrcodeimage",
                outputfile);
        mailSender.send(message);

        }

        @Test
    public void mailTestSender() throws Exception {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("smtp.mailtrap.io");
        mailSender.setPort(465);
        mailSender.setUsername("b55f580d63c202");
        mailSender.setPassword("e8bbb728c2fbc5");

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);
        SimpleMailMessage sender = new SimpleMailMessage();

        helper.setFrom("smtp.mailtrap.io");
        helper.setTo("kadirtayyiperkan@hotmail.com");
        helper.setSubject("YOUR QRCODE FOR JOINING THE EVENT");
        helper.setText("<html>"
                    +"<body>"
                    +"<div>Dear user, %s" +
                    "<div>" +
                    "<img src='cid:qrcodeimage'/>" +
                    "</div>" +
                    "</div>" +
                    "</body>" +
                    "</html>",true);

        BufferedImage qrCode = QRcodeUtil.generateQRCodeImage("TEST TEST TEST TEST");

        File outputfile = new File("image.jpg");

        ImageIO.write(qrCode, "jpg", outputfile);

        helper.addInline("qrcodeimage", outputfile);

        mailSender.send(message);

    }


}
