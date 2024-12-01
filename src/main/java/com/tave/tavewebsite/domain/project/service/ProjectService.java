package com.tave.tavewebsite.domain.project.service;

import com.tave.tavewebsite.domain.project.dto.request.ProjectRequestDto;
import com.tave.tavewebsite.domain.project.dto.response.ProjectResponseDto;
import com.tave.tavewebsite.domain.project.entity.Project;
import com.tave.tavewebsite.domain.project.exception.ProjectNotFoundException;
import com.tave.tavewebsite.domain.project.repository.ProjectRepository;
import com.tave.tavewebsite.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final S3Service s3Service;

    public ProjectResponseDto createProject(ProjectRequestDto requestDto, MultipartFile imageFile) {
        URL imgUrl = s3Service.uploadImages(imageFile);
        Project project = Project.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .generation(requestDto.getGeneration())
                .teamName(requestDto.getTeamName())
                .field(requestDto.getField())
                .blogUrl(requestDto.getBlogUrl())
                .imgUrl(imgUrl.toString())
                .build();
        projectRepository.save(project);
        return new ProjectResponseDto(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponseDto> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(ProjectResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProjectResponseDto getProjectById(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project ID: " + projectId));
        return new ProjectResponseDto(project);
    }

    public ProjectResponseDto updateProject(Long projectId, ProjectRequestDto requestDto, MultipartFile imageFile) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project ID: " + projectId));

        URL imgUrl = s3Service.uploadImages(imageFile);
        project.update(requestDto.getTitle(), requestDto.getDescription(), requestDto.getGeneration(),
                requestDto.getTeamName(), requestDto.getField(), requestDto.getBlogUrl(), imgUrl.toString());

        return new ProjectResponseDto(project);
    }

    public void deleteProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project ID: " + projectId));
        s3Service.deleteImage(project.getImgUrl());
        projectRepository.delete(project);
    }
}
