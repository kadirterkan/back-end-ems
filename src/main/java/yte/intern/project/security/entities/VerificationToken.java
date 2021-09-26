package yte.intern.project.security.entities;

import lombok.Getter;
import yte.intern.project.user.entities.BaseUser;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@Entity
@Getter
public class VerificationToken {

    public VerificationToken(String token, BaseUser user) {
        this.token = token;
        this.user = user;
        Calendar currentTimeNow = Calendar.getInstance();
        currentTimeNow.add(Calendar.HOUR, EXPIRATION);
        this.expiryDate = currentTimeNow.getTime();
    }

    private static final int EXPIRATION = 4*24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @ManyToOne
    private BaseUser user;

    private Date expiryDate;

    public VerificationToken() {

    }

    private Date calculateExpiryDate(int expiryTimeInMinutes){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE,expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }
}
