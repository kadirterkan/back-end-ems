package yte.intern.project.user.entities;

import lombok.ToString;
import yte.intern.project.event.entities.UserEvent;
import yte.intern.project.event.entities.CustomEvent;
import yte.intern.project.event.entities.QuestionAnswers;
import yte.intern.project.event.entities.Questionnaire;
import yte.intern.project.user.enums.RoleEnum;
import yte.intern.project.user.enums.Departments;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@ToString
public class CustomUser extends BaseUser {

    public CustomUser(String username,
                      String firstName,
                      String lastName,
                      String email,
                      String password,
                      String tcKimlikNumber,
                      Departments departments) {
        super(username,
                firstName,
                lastName,
                email,
                password,
                RoleEnum.USER,
                departments);
        this.tcKimlikNumber = tcKimlikNumber;
    }


    @OneToMany
    @MapsId("user")
    private Set<UserEvent> events;

    @ManyToMany
    @JoinTable(
            name = "interested_events",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private Set<CustomEvent> interestedEvents  = new HashSet<>();

//    @ManyToMany
//    @JoinTable(
//            name = "saved_events",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "event_id"))
//    private Set<CustomEvent> savedEvents = new HashSet<>();

    @OneToMany(mappedBy = "answeredByUser")
    private Set<Questionnaire> eventQuestionnaire = new HashSet<>();

    @OneToMany(mappedBy = "answeredQuestionsByUser")
    private Set<QuestionAnswers> questionAnswers = new HashSet<>();

    private String tcKimlikNumber;

    public CustomUser() {
        super(RoleEnum.USER);
    }

    public String getTcKimlikNumber() {
        return tcKimlikNumber;
    }

    public void addEventToUser(UserEvent userEvent){
        events.add(userEvent);
    }

    public void addInterestedEvent(CustomEvent customEvent) {
        interestedEvents.add(customEvent);
    }

//    public void addSavedEvent(CustomEvent customEvent){
//        savedEvents.add(customEvent);
//    }

    public void addEventQuestionnaire(Questionnaire questionnaire){
        eventQuestionnaire.add(questionnaire);
    }

    public void addQuestionAnswers(QuestionAnswers answers){
        questionAnswers.add(answers);
    }

    public void deleteInterestedEvent(CustomEvent customEvent) {
        interestedEvents.remove(customEvent);
    }

//    public void deleteSavedEvent(CustomEvent customEvent){
//        savedEvents.remove(customEvent);
//    }


    public boolean leaveEvent(UserEvent userEvent){
        return events.remove(userEvent);
    }

    @Override
    public String toString() {
        return "CustomUser{" +
                "tcKimlikNumber='" + tcKimlikNumber + '\'' +
                '}';
    }
}
