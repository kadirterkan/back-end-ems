package yte.intern.project.event.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import yte.intern.project.event.enums.EventPrivacy;
import yte.intern.project.user.entities.CustomMod;
import yte.intern.project.user.entities.CustomUser;
import yte.intern.project.user.enums.Departments;

import javax.persistence.*;
import javax.validation.constraints.Future;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class CustomEvent {

    public CustomEvent(Long id,
                       Long quota,
                       String eventName,
                       String base64Image,
                       String eventCategory,
                       Departments eventPrivacy,
                       String eventDescription,
                       String eventDetail,
                       LocalDateTime startTime,
                       LocalDateTime endTime,
                       CustomMod createdBy,
                       List<String> questions) {
        this.id = id;
        this.quota = quota;
        this.eventName = eventName;
        this.base64Image = base64Image;
        this.eventCategory = eventCategory;
        this.eventPrivacy = eventPrivacy;
        this.eventDescription = eventDescription;
        this.eventDetail = eventDetail;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createdBy = createdBy;
        this.questions = questions;
    }

    public CustomEvent(Long quota,
                       String eventName,
                       String base64Image,
                       String eventCategory,
                       Departments eventPrivacy,
                       String eventDescription,
                       String eventDetail,
                       LocalDateTime startTime,
                       LocalDateTime endTime,
                       CustomMod createdBy,
                       List<String> questions) {
        this.quota = quota;
        this.eventName = eventName;
        this.base64Image = base64Image;
        this.eventCategory = eventCategory;
        this.eventPrivacy = eventPrivacy;
        this.eventDescription = eventDescription;
        this.eventDetail = eventDetail;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createdBy = createdBy;
        this.questions = questions;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected Long quota;

    protected String eventName;

    @Lob
    @Column( length = 100000 )
    protected String base64Image = null;

    protected String eventCategory;

    protected Departments eventPrivacy;

    protected String eventDescription;

    protected String eventDetail;

    @Future
    protected LocalDateTime startTime;

    @Future
    protected LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "creater",referencedColumnName = "id")
    protected CustomMod createdBy;

    @ManyToMany(mappedBy = "interestedEvents")
    protected Set<CustomUser> interestedUsers = new HashSet<>();

//    @ManyToMany(mappedBy = "savedEvents")
//    protected  Set<CustomUser> savedUsers = new HashSet<>();

    @OneToMany
    @MapsId("event")
    protected Set<UserEvent> users = new HashSet<>();

    @OneToMany(mappedBy = "answeredToEvent")
    protected Set<Questionnaire> questionnaires = new HashSet<>();


    @ElementCollection
    private List<String> askedQuestionsByUsers = new ArrayList<>();

    @ElementCollection
    private List<String> questions = new ArrayList<>();

    @OneToMany(mappedBy = "answeredQuestionsByUser")
    private Set<QuestionAnswers> questionAnswers = new HashSet<>();

//    public void addSavedUser (CustomUser customUser){
//        interestedUsers.add(customUser);
//    }
//
//    public void deleteSavedUser (CustomUser customUser){
//        interestedUsers.remove(customUser);
//    }

    public void addInterestedUser (CustomUser customUser){
        interestedUsers.add(customUser);
    }

    public void deleteInterestedUser (CustomUser customUser){
        interestedUsers.remove(customUser);
    }

    public void addQuestionnaire(Questionnaire question){
        questionnaires.add(question);
    }

    public void addQuestionAnswers (QuestionAnswers questionAnswer){
        questionAnswers.add(questionAnswer);
    }

    public void addQuestionFromUser(String question){
        this.askedQuestionsByUsers.add(question);
    }

    public void addUserToEvent(UserEvent userEvent){
        this.users.add(userEvent);
    }

    public boolean isNotFull(){
        return users.size()<quota;
    }

    public boolean deleteUserFromEvent(UserEvent userEvent){
        return users.remove(userEvent);
    }

    @Override
    public String toString() {
        return "CustomEvent{" +
                "quota=" + quota +
                ", eventName='" + eventName + '\'' +
                ", createdBy=" + createdBy +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", currently people attending" + users.size() +
                "}\n";
    }
}