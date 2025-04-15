package com.tave.tavewebsite.domain.resume.repository;

import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.entity.ResumeQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ResumeQuestionJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public int[] bulkInsert(List<ResumeQuestion> questions, Resume resume) {

        // INSERT 쿼리
        String sql = """
            INSERT INTO resume_question (question, answer, ordered, resume_id, field_type, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, NOW(), NOW())
        """;

        return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ResumeQuestion resumeQuestion = questions.get(i);
                ps.setString(1, resumeQuestion.getQuestion());
                ps.setString(2, resumeQuestion.getAnswer());
                if (resumeQuestion.getOrdered() != null) {
                    ps.setInt(3, resumeQuestion.getOrdered());
                } else {
                    ps.setNull(3, Types.INTEGER);
                }
                ps.setLong(4, resume.getId());
                ps.setString(5, resumeQuestion.getFieldType().name());
            }

            @Override
            public int getBatchSize() {
                return questions.size();
            }
        });
    }
}
