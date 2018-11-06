package com.jumpingstone.codequality.fireeye.service.controller;

import com.jumpingstone.codequality.fireeye.model.IProject;
import com.jumpingstone.codequality.fireeye.model.ManagedProject;
import com.jumpingstone.codequality.fireeye.service.ProjectService;
import com.jumpingstone.codequality.fireeye.service.model.GitProjectDefinition;
import com.jumpingstone.codequality.fireeye.service.model.ProjectDefinition;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://10.58.91.167:3000"
    }, methods={RequestMethod.GET, RequestMethod.POST})
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectManager;

    @PostMapping("/create/{project_id}")
    private Mono<IProject> createProject(@PathVariable String project_id, @RequestBody ProjectDefinition projectDefinition) {
        return projectManager.createProject(project_id, projectDefinition);
    }

    @PostMapping("/clone")
    private Mono<IProject> cloneProject(@RequestBody GitProjectDefinition projectDefinition) {
        return projectManager.cloneProject(projectDefinition.getGitRepo(),
                projectDefinition.getUsername(),
                projectDefinition.getPassword());
    }

    @PostMapping("/scan/{project_id}")
    private Mono<ManagedProject> scanProject(@PathVariable String project_id) {
        return projectManager.scanProject(project_id);
    }

    @GetMapping("")
    private Flux<IProject> getProjects() {
        return projectManager.findProject(ProjectMatchers.matchAny());
    }

    @GetMapping("/{project_id}")
    private Mono<IProject> getProject(@PathVariable String project_id) {
        return projectManager.getProject(project_id);
    }

    @GetMapping("/count/name/like/{name}")
    private Integer countProjectNmae(@PathVariable String name) {
        int count = projectManager.countProject(ProjectMatchers.matchName(name));
        return count;
    }

    @GetMapping("/count/path/like/{name}")
    private Integer countProjectPath(@PathVariable String path) {
        int count = projectManager.countProject(ProjectMatchers.matcherPath(path));
        return count;
    }

    @GetMapping("/name/like/{name}")
    private Flux<IProject> findProjectsByName(@PathVariable String name) {
        return projectManager.findProject(ProjectMatchers.matchName(name));
    }

    @GetMapping("/path/like/{name}")
    private Flux<IProject> findProjectsByPath(@PathVariable String path) {
        return projectManager.findProject(ProjectMatchers.matcherPath(path));
    }

    private static class ProjectMatchers {
        public static Matcher matchName(String name) {
            return new BaseMatcher<IProject>() {
                @Override
                public boolean matches(Object o) {
                    IProject project = (IProject)o;
                    return project.getName().toLowerCase().contains(name.toLowerCase());
                }

                @Override
                public void describeTo(Description description) {

                }
            };
        }

        public static Matcher matcherPath(String path) {
            return new BaseMatcher<IProject>() {
                @Override
                public boolean matches(Object o) {
                    IProject project = (IProject)o;
                    return project.getPath().toAbsolutePath().toString().toLowerCase().contains(path.toLowerCase());
                }

                @Override
                public void describeTo(Description description) {

                }
            };
        }

        public static Matcher matchAny() {
            return new BaseMatcher<IProject>() {
                @Override
                public boolean matches(Object o) {
                    return true;
                }

                @Override
                public void describeTo(Description description) {

                }
            };
        }
    }
}
