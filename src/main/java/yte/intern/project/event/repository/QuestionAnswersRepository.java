package yte.intern.project.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yte.intern.project.event.entities.CustomEvent;
import yte.intern.project.event.entities.QuestionAnswers;

import java.util.Optional;

public interface QuestionAnswersRepository extends JpaRepository<QuestionAnswers, Long> {

    @Override
    QuestionAnswers getById(Long aLong);

    boolean removeAllByEvent(CustomEvent customEvent);
}