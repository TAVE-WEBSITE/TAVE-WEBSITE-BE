package com.tave.tavewebsite.domain.project.controller;

import com.tave.tavewebsite.domain.project.dto.request.ProjectRequestDto;
import com.tave.tavewebsite.domain.project.dto.response.ProjectResponseDto;
import com.tave.tavewebsite.domain.project.service.ProjectService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public SuccessResponse<ProjectResponseDto> createProject(@RequestBody @Valid ProjectRequestDto requestDto) {
        ProjectResponseDto response = projectService.createProject(requestDto);
        return new SuccessResponse<>(response);
    }

    @GetMapping
    public SuccessResponse<List<ProjectResponseDto>> getAllProjects() {
        List<ProjectResponseDto> response = projectService.getAllProjects();
        return new SuccessResponse<>(response);
    }
}
