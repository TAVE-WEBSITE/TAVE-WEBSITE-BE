package com.tave.tavewebsite.domain.question.repository;

import com.tave.tavewebsite.domain.question.entity.Question;
import com.tave.tavewebsite.global.common.FieldType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findAllByOrderByFieldTypeAscOrderedAscContentAsc();

    List<Question> findQuestionByFieldTypeOrderByOrderedAscContentAsc(FieldType fieldType);

    @Query("SELECT MAX(q.ordered) FROM Question q WHERE q.fieldType = :fieldType")
    Optional<Integer> findMaxOrderedByFieldType(@Param("fieldType") FieldType fieldType);

}
