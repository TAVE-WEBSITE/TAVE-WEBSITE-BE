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
        return SuccessResponse.ok("프로젝트가 생성되었습니다.");
    }

    @GetMapping("/normal/project")
    public SuccessResponse<List<ProjectResponseDto>> getAllProjects() {
        List<ProjectResponseDto> response = projectService.getAllProjects();
        return new SuccessResponse<>(response);
    }

    @GetMapping("/normal/project/{projectId}")
    public SuccessResponse<ProjectResponseDto> getProjectById(@PathVariable Long projectId) {
        ProjectResponseDto response = projectService.getProjectById(projectId);
        return new SuccessResponse<>(response);
    }

    @PutMapping("/manager/project/{projectId}")
    public SuccessResponse updateProject(@PathVariable Long projectId,
                                         @RequestPart @Valid ProjectRequestDto requestDto,
                                         @RequestPart MultipartFile imageFile) {
        projectService.updateProject(projectId, requestDto, imageFile);
        return SuccessResponse.ok("프로젝트가 수정되었습니다.");
    }

    @DeleteMapping("/manager/project/{projectId}")
    public SuccessResponse deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return SuccessResponse.ok("프로젝트가 성공적으로 삭제되었습니다.");
    }
}
