package com.tave.tavewebsite.domain.project.controller;

import com.tave.tavewebsite.domain.project.dto.request.ProjectRequestDto;
import com.tave.tavewebsite.domain.project.dto.response.ProjectResponseDto;
import com.tave.tavewebsite.domain.project.service.ProjectService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/manager/project")
    public SuccessResponse createProject(@RequestPart @Valid ProjectRequestDto requestDto, @RequestPart MultipartFile imageFile) {
        projectService.createProject(requestDto, imageFile);
        return SuccessResponse.ok(SuccessMessage.PROJECT_CREATE.getMessage());
    }

    @GetMapping("/normal/project")
    public SuccessResponse<List<ProjectResponseDto>> getAllProjects() {
        List<ProjectResponseDto> response = projectService.getAllProjects();
        return new SuccessResponse<>(response, SuccessMessage.PROJECT_READ.getMessage());
    }

    @GetMapping("/normal/project/{projectId}")
    public SuccessResponse<ProjectResponseDto> getProjectById(@PathVariable Long projectId) {
        ProjectResponseDto response = projectService.getProjectById(projectId);
        return new SuccessResponse<>(response, SuccessMessage.PROJECT_READ.getMessage());
    }

    @PutMapping("/manager/project/{projectId}")
    public SuccessResponse updateProject(@PathVariable Long projectId,
                                         @RequestPart @Valid ProjectRequestDto requestDto,
                                         @RequestPart MultipartFile imageFile) {
        projectService.updateProject(projectId, requestDto, imageFile);
        return SuccessResponse.ok(SuccessMessage.PROJECT_UPDATE.getMessage());
    }

    @DeleteMapping("/manager/project/{projectId}")
    public SuccessResponse deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return SuccessResponse.ok(SuccessMessage.PROJECT_DELETE.getMessage());
    }
}