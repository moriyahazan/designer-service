package com.idit.designer.project_manager.controller;

import com.idit.designer.project_manager.model.Project;
import com.idit.designer.project_manager.repository.ProjectRepository;
import com.idit.designer.project_manager.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/projects")
    @Operation(summary = "Create new project")
    Project createNewProject(@RequestBody Project parentProject){
        return projectService.createNewProject(parentProject);
    }


}
