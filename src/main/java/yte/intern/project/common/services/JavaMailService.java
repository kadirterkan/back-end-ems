package yte.intern.project.common.services;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import yte.intern.project.common.dto.MessageResponse;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

@Service
public class JavaMailService {
    private final Properties prop;

    public JavaMailService() {
        this.prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.mailtrap.io");
        prop.put("mail.smtp.port", "25");
        prop.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");
    }

        public void mailSender() throws MessagingException {
            Session session = Session.getInstance(prop);

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("kadirtayyiperkan@hotmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse("kadirtayyiperkan@hotmail.com"));
            message.setSubject("Mail Subject");

            String msg = "This is my first email using JavaMailer";

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(msg, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);

        }
}
