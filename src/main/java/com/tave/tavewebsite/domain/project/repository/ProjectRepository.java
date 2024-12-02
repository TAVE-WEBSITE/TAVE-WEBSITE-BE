package com.tave.tavewebsite.domain.project.repository;

import com.tave.tavewebsite.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long>, CustomProjectRepository {

}
