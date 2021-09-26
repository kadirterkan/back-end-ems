package yte.intern.project.event.entities;


import lombok.Getter;
import yte.intern.project.user.entities.CustomUser;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
public class QuestionAnswers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<String> answers = new ArrayList<>();

    @ManyToOne
    private CustomEvent event;

    @ManyToOne
    private CustomUser answeredQuestionsByUser;

    private LocalDateTime localDateTime;


    public QuestionAnswers(List<String> answers, CustomEvent event, CustomUser answeredQuestionsByUser, LocalDateTime localDateTime) {
        this.answers = answers;
        this.event = event;
        this.answeredQuestionsByUser = answeredQuestionsByUser;
        this.localDateTime = localDateTime;
    }
}
