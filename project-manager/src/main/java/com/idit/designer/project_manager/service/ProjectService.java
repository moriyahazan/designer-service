package com.idit.designer.project_manager.service;

import com.idit.designer.project_manager.model.Project;
import org.springframework.data.domain.Page;

public interface ProjectService {

    Project createNewProject(Project parentProject);
}
