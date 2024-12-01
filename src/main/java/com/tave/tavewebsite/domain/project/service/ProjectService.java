package com.tave.tavewebsite.domain.project.service;

import com.tave.tavewebsite.domain.project.dto.request.ProjectRequestDto;
import com.tave.tavewebsite.domain.project.dto.response.ProjectResponseDto;
import com.tave.tavewebsite.domain.project.entity.Project;
import com.tave.tavewebsite.domain.project.exception.ProjectNotFoundException;
import com.tave.tavewebsite.domain.project.repository.ProjectRepository;
import com.tave.tavewebsite.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final S3Service s3Service;

    // 프로젝트 생성
    public void createProject(ProjectRequestDto req, MultipartFile file) {
        // 파일을 S3에 업로드하고 URL을 받음
        URL url = s3Service.uploadImages(file);

        // ProjectRequestDto에서 값을 추출하여 Project 엔티티 생성
//        Project project = new Project(req.getTitle(), req.getDescription(), req.getGeneration(), req.getFieldType(), url);
        Project project = new Project(req, url);

        projectRepository.save(project);
    }

    // 프로젝트 조회
    @Transactional(readOnly = true)
    public Page<ProjectResponseDto> getProjects(String generation, String field, Pageable pageable) {
        Page<ProjectResponseDto> projects;

        try {
            log.info("field: {}, generation: {}", field, generation);

            // 필드와 세대 조건에 맞는 프로젝트 찾기
            if ("ALL".equals(generation) && "ALL".equals(field)) {
                projects = projectRepository.findAllProjects(pageable);
            } else if ("ALL".equals(generation)) {
                projects = projectRepository.findProjectByField(field, pageable);
            } else if ("ALL".equals(field)) {
                projects = projectRepository.findProjectByGeneration(generation, pageable);
            } else {
                projects = projectRepository.findProjectByGenerationAndField(generation, field, pageable);
            }
        } catch (Exception e) {
            // 예외 발생 시 PROJECT_NOT_FOUND 예외 던지기
            throw new ProjectNotFoundException();
        }

        return projects;
    }

    // 프로젝트 수정
    public void updateProject(Long projectId, ProjectRequestDto req, MultipartFile file) {
        // 프로젝트가 존재하는지 확인
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new); // ProjectNotFoundException 발생

        // 파일을 S3에 업로드하고 URL을 받음
        URL url = s3Service.uploadImages(file);

        // 프로젝트 수정
        project.updateProject(req, url);
    }

    // 프로젝트 삭제
    public void deleteProject(Long projectId) {
        // 프로젝트가 존재하는지 확인
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        // S3에서 이미지 삭제
        s3Service.deleteImage(project.getImgUrl());

        // 프로젝트 삭제
        projectRepository.delete(project);
    }
}
