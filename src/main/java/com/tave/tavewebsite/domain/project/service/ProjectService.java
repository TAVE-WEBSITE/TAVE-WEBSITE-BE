package com.tave.tavewebsite.domain.project.service;

import com.tave.tavewebsite.domain.project.dto.request.ProjectRequestDto;
import com.tave.tavewebsite.domain.project.dto.response.ProjectResponseDto;
import com.tave.tavewebsite.domain.project.entity.Project;
import com.tave.tavewebsite.domain.project.exception.ProjectNotFoundException;
import com.tave.tavewebsite.domain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectResponseDto createProject(ProjectRequestDto requestDto) {
        Project project = Project.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .generation(requestDto.getGeneration())
                .teamName(requestDto.getTeamName())
                .field(requestDto.getField())
                .blogUrl(requestDto.getBlogUrl())
                .imgUrl(requestDto.getImgUrl())
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
                .orElseThrow(() -> new ProjectNotFoundException("Project ID: " + projectId + projectId));
        return new ProjectResponseDto(project);
    }

    public ProjectResponseDto updateProject(Long projectId, ProjectRequestDto requestDto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project ID: " + projectId));

        project.update(requestDto.getTitle(), requestDto.getDescription(), requestDto.getGeneration(),
                requestDto.getTeamName(), requestDto.getField(), requestDto.getBlogUrl(), requestDto.getImgUrl());

        return new ProjectResponseDto(project);
    }

    public void deleteProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project ID: " + projectId));
        projectRepository.delete(project);
    }
}
