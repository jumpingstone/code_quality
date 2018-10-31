package com.jumpingstone.codequality.fireeye.service.controller;

import com.jumpingstone.codequality.fireeye.Project;
import com.jumpingstone.codequality.fireeye.service.ProjectService;
import com.jumpingstone.codequality.fireeye.service.model.ProjectDefinition;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectManager;

    @PostMapping("/{project_id}")
    private Mono<Project> createProject(@PathVariable String project_id, @RequestBody ProjectDefinition projectDefinition) {
        return projectManager.createProject(project_id, projectDefinition);
    }

    @GetMapping("/{project_id}")
    private Mono<Project> getProject(@PathVariable String project_id) {
        return projectManager.getProject(project_id);
    }

    @GetMapping("/count/name/like/{name}")
    private Integer countProjectNmae(@PathVariable String name) {
        int count = projectManager.countProject(ProjectMatchers.matchName(name));
        return count;
    }

    @GetMapping("/count/name/like/{name}")
    private Integer countProjectPath(@PathVariable String path) {
        int count = projectManager.countProject(ProjectMatchers.matcherPath(path));
        return count;
    }

    @GetMapping("/name/like/{name}")
    private Flux<Project> findProjectsByName(@PathVariable String name) {
        return projectManager.findProject(ProjectMatchers.matchName(name));
    }

    @GetMapping("/path/like/{name}")
    private Flux<Project> findProjectsByPath(@PathVariable String path) {
        return projectManager.findProject(ProjectMatchers.matcherPath(path));
    }

    private static class ProjectMatchers {
        public static Matcher matchName(String name) {
            return new BaseMatcher<Project>() {
                @Override
                public boolean matches(Object o) {
                    Project project = (Project)o;
                    return project.getName().toLowerCase().contains(name.toLowerCase());
                }

                @Override
                public void describeTo(Description description) {

                }
            };
        }

        public static Matcher matcherPath(String path) {
            return new BaseMatcher<Project>() {
                @Override
                public boolean matches(Object o) {
                    Project project = (Project)o;
                    return project.getPath().toAbsolutePath().toString().toLowerCase().contains(path.toLowerCase());
                }

                @Override
                public void describeTo(Description description) {

                }
            };
        }
    }
}
