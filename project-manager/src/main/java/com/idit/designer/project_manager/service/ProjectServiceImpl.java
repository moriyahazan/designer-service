package com.idit.designer.project_manager.service;

import com.idit.designer.project_manager.model.Project;
import com.idit.designer.project_manager.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService{

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public Project createNewProject(Project project) {
        ZonedDateTime now = ZonedDateTime.now();
        project.setCreatedOn(now);
        return projectRepository.save(project);
    }
}
