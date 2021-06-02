package com.idit.designer.project_manager.repository;

import com.idit.designer.project_manager.model.Project;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends CouchbaseRepository<Project, String> {

}
