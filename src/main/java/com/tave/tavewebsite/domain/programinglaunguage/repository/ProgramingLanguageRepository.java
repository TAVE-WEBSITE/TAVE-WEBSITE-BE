package com.tave.tavewebsite.domain.programinglaunguage.repository;

import com.tave.tavewebsite.domain.programinglaunguage.entity.ProgramingLanguage;
import com.tave.tavewebsite.global.common.FieldType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramingLanguageRepository extends JpaRepository<ProgramingLanguage, Long> {
    List<ProgramingLanguage> findByField(FieldType field);
}
