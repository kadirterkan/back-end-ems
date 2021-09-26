package yte.intern.project.event.entities;


import lombok.Getter;
import yte.intern.project.event.embeddable.UserEventId;
import yte.intern.project.user.entities.CustomUser;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
public class Questionnaire {

    @EmbeddedId
    private UserEventId id = new UserEventId();

    @ElementCollection
    private List<Long> points;

    @ManyToOne
    @MapsId("eventId")
    private CustomEvent answeredToEvent;

    @ManyToOne
    @MapsId("userId")
    private CustomUser answeredByUser;

    public Questionnaire(List<Long> points) {
        this.points = points;
    }

    public Questionnaire() {

    }
}
