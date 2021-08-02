package yte.intern.project.common.configuration;

import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class JavaMailConfiguration {

    private Properties prop;

    public JavaMailConfiguration() {
        this.prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.mailtrap.io");
        prop.put("mail.smtp.port", "25");
        prop.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");

    }
}
