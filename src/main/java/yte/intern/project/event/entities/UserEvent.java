package yte.intern.project.event.entities;

import lombok.Getter;
import lombok.Setter;
import yte.intern.project.event.embeddable.UserEventId;
import yte.intern.project.user.entities.CustomUser;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class UserEvent {

    @EmbeddedId
    private UserEventId id = new UserEventId();

    @ManyToOne
    @MapsId("userId")
    private CustomUser user;

    @ManyToOne
    @MapsId("eventId")
    private CustomEvent event;

    private LocalDateTime joinTime;

    @Lob
    @Column( length = 100000 )
    private String base64QrCode;

    @Override
    public String toString() {
        return "{" +
                "joinTime=" + joinTime +
                '}';
    }

    public UserEvent() {

    }

    public UserEvent(UserEventId id, LocalDateTime joinTime, String base64QrCode) {
        this.id = id;
        this.joinTime = joinTime;
        this.base64QrCode = base64QrCode;
    }
}
