package yte.intern.project.common.entities;

import lombok.Getter;
import lombok.Setter;
import yte.intern.project.common.embeddable.UserEventId;
import yte.intern.project.event.entities.CustomEvent;
import yte.intern.project.user.entities.CustomUser;

import javax.persistence.*;
import java.awt.image.BufferedImage;
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

    @Override
    public String toString() {
        return "{" +
                "joinTime=" + joinTime +
                '}';
    }

    public UserEvent() {

    }

    public UserEvent(UserEventId id, CustomUser user, CustomEvent event, LocalDateTime joinTime) {
        this.id = id;
        this.user = user;
        this.event = event;
        this.joinTime = joinTime;
    }
}
