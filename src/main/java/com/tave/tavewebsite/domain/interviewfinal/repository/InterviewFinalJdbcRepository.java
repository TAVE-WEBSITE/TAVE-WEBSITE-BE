package com.tave.tavewebsite.domain.interviewfinal.repository;

import com.tave.tavewebsite.domain.interviewfinal.dto.InterviewFinalConvertDto;
import com.tave.tavewebsite.domain.interviewfinal.dto.InterviewFinalSaveDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class InterviewFinalJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public int[] bulkInsert(List<InterviewFinalSaveDto> dtoList) {

        // INSERT 쿼리
        String sql = """
            INSERT INTO interview_final (username, last_tel, generation, sex, university, field_type, member_id, resume_id, interview_day, interview_time , created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())
        """;

        return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                InterviewFinalSaveDto dto = dtoList.get(i);
                ps.setString(1, dto.username());
                ps.setString(2, dto.email());
                ps.setInt(3, dto.generation());
                ps.setString(4, dto.sex().name());
                ps.setString(5, dto.university());
                ps.setString(6, dto.fieldType().name());
                ps.setInt(7, dto.memberId().intValue());
                ps.setInt(8, dto.resumeId().intValue());
                ps.setString(9, dto.interviewDay());
                ps.setString(10, dto.interviewTime());
            }

            @Override
            public int getBatchSize() {
                return dtoList.size();
            }
        });
    }
}
