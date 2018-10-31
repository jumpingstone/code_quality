package com.jumpingstone.codequality.fireeye.service;

import com.jumpingstone.codequality.fireeye.model.IProject;
import com.jumpingstone.codequality.fireeye.service.model.ProjectDefinition;
import org.hamcrest.Matcher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProjectService {
    Mono<IProject> createProject(String project_id, ProjectDefinition projectDefinition);

    int countProject(Matcher<IProject> matcher);

    Flux<IProject> findProject(Matcher<IProject> matcher);

    Mono<IProject> getProject(String project_id);

    Mono<IProject> scanProject(String project_id);

}
