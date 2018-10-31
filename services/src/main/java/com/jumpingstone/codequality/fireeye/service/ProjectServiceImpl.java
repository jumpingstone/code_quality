package com.jumpingstone.codequality.fireeye.service;

import com.jumpingstone.codequality.fireeye.Project;
import com.jumpingstone.codequality.fireeye.ProjectManager;
import com.jumpingstone.codequality.fireeye.service.model.ProjectDefinition;
import org.hamcrest.Matcher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ProjectServiceImpl implements ProjectService {
    ProjectManager projectManager = new ProjectManager();

    public Mono<Project> getProject(String project_id) {
        return Mono.just(new Project());
    }

    public Flux<Project> findProject(Matcher matcher) {

    }

    public int countProject(Matcher matcher) {
    }

    public Mono<Project> createProject(String project_id, ProjectDefinition projectDefinition) {
        Project project =
        return null;
    }
}
