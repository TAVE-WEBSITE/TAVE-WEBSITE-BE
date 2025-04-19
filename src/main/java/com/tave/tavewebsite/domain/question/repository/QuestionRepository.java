package com.tave.tavewebsite.domain.question.repository;

import com.tave.tavewebsite.domain.question.entity.Question;
import com.tave.tavewebsite.global.common.FieldType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findAllByOrderByFieldTypeAscOrderedAscContentAsc();

    List<Question> findQuestionByFieldTypeOrderByOrderedAscContentAsc(FieldType fieldType);
}
