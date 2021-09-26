package yte.intern.project.event.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yte.intern.project.event.entities.CustomEvent;
import yte.intern.project.event.entities.QuestionAnswers;
import yte.intern.project.event.repository.QuestionAnswersRepository;

@Service
public class QuestionAnswersService {

    private final QuestionAnswersRepository questionAnswersRepository;

    @Autowired
    public QuestionAnswersService(QuestionAnswersRepository questionAnswersRepository) {
        this.questionAnswersRepository = questionAnswersRepository;
    }

    public void addNewAnswers(QuestionAnswers questionAnswers){
        questionAnswersRepository.save(questionAnswers);
    }

    public void removeAllAnswers(CustomEvent customEvent){
        questionAnswersRepository.removeAllByEvent(customEvent);
    }
}
