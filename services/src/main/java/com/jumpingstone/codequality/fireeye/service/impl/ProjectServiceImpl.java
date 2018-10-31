package com.jumpingstone.codequality.fireeye.service.impl;

import com.jumpingstone.codequality.fireeye.ObjectFactory;
import com.jumpingstone.codequality.fireeye.ProjectManager;
import com.jumpingstone.codequality.fireeye.model.IProject;
import com.jumpingstone.codequality.fireeye.service.ProjectService;
import com.jumpingstone.codequality.fireeye.service.model.ProjectDefinition;
import org.hamcrest.Matcher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

public class ProjectServiceImpl implements ProjectService {

    ProjectManager projectManager = new ObjectFactory().createProjectManager(System.getProperty("user.home") +
            File.separator + ".jp_sim_graphic_db");

    @Override
    public Mono<IProject> createProject(String project_id, ProjectDefinition projectDefinition) {
        IProject project = projectManager.createProject(projectDefinition.getName(),
                Paths.get(projectDefinition.getPath()));
        return Mono.just(project);
    }

    @Override
    public Mono<IProject> getProject(String project_id) {
        IProject project = projectManager.getProject(project_id);
        return Mono.just(project);
    }

    @Override
    public Mono<IProject> scanProject(String project_id) {
        IProject project = projectManager.scanProject(project_id);
        return Mono.just(project);
    }

    @Override
    public Flux<IProject> findProject(Matcher<IProject> matcher) {
        Iterable<IProject> projects = projectManager.findProject(matcher);
        return Flux.fromIterable(projects);
    }

    public int countProject(Matcher<IProject> matcher) {
        List<IProject> projects = projectManager.findProject(matcher);
        return projects.size();
    }
}
