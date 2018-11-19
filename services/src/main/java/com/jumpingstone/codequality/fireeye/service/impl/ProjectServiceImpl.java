package com.jumpingstone.codequality.fireeye.service.impl;

import com.jumpingstone.codequality.fireeye.ObjectFactory;
import com.jumpingstone.codequality.fireeye.ProjectManager;
import com.jumpingstone.codequality.fireeye.model.IProject;
import com.jumpingstone.codequality.fireeye.model.ManagedProject;
import com.jumpingstone.codequality.fireeye.model.Similarity;
import com.jumpingstone.codequality.fireeye.service.ProjectService;
import com.jumpingstone.codequality.fireeye.service.SimilarityResponse;
import com.jumpingstone.codequality.fireeye.service.SimilarityService;
import com.jumpingstone.codequality.fireeye.service.controller.SimilarityStatisticResponse;
import com.jumpingstone.codequality.fireeye.service.model.ProjectDefinition;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.hamcrest.Matcher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ProjectServiceImpl implements ProjectService, SimilarityService {

    private final String gitCodeBase = System.getProperty("user.home") + File.separator + "git_code_base";

    private ProjectManager projectManager = new ObjectFactory().createProjectManager(
            System.getProperty("user.home") + File.separator + ".jp_sim_graphic_db",
            gitCodeBase
            );

    @Override
    public Mono<IProject> createProject(String project_id, ProjectDefinition projectDefinition) {
        IProject project = projectManager.createProject(projectDefinition.getName(),
                Paths.get(projectDefinition.getPath()));
        return Mono.just(project);
    }

    @Override
    public Mono<IProject> cloneProject(String projectURI, String username, String password) {
        Mono<IProject> result = null;
        if (!Files.exists(Paths.get(gitCodeBase))) {
            try {
                Files.createDirectory(Paths.get(gitCodeBase));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        int lastIndex = projectURI.lastIndexOf('/');
        String name = projectURI.substring(lastIndex + 1);
        if (name.endsWith(".git")) {
            name = name.substring(0, name.length() - 4);
        }
        String gitDir = gitCodeBase + File.separator + name;
        if (!Files.exists(Paths.get(gitDir))) {
            try {
                Files.createDirectory(Paths.get(gitDir));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            CloneCommand cloneCommand = Git.cloneRepository()
                    .setURI( projectURI )
                    .setDirectory(Paths.get(gitDir).toFile());

            if (StringUtils.isNotBlank(username)) {
                cloneCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password));
            }
            Git git = cloneCommand.call();
            File gitDirectory = git.getRepository().getDirectory();
            IProject project = projectManager.createProject(gitDirectory.getName(),
                    Paths.get(gitDirectory.getPath()));
            result = Mono.just(project);
        } catch (GitAPIException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Mono<IProject> getProject(String project_id) {
        IProject project = projectManager.getProject(project_id);
        return Mono.just(project);
    }

    @Override
    public Mono<ManagedProject> scanProject(String project_id) {
        ManagedProject project = projectManager.scanProject(project_id);
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

    @Override
    public Mono<SimilarityStatisticResponse> getSimilarityStatistic(String project_id) {
        Map<Float, Integer> similarities = projectManager.getSimilarityStatistic(project_id);
        return Mono.just(new SimilarityStatisticResponse(project_id, similarities));
    }

    @Override
    public Mono<SimilarityResponse> findSimilarityFiles(String project_id, Float threshold) {
        Set<Similarity> similarities = projectManager.findSimilarityFiles(project_id, threshold);
        return Mono.just(new SimilarityResponse(project_id, similarities));
    }

    @Override
    public Mono<SimilarityResponse> findSimilarityFiles(String project_id, Integer file_id, Float threshold) {
        Set<Similarity> similarities = projectManager.findSimilarityFiles(project_id, file_id, threshold);
        return Mono.just(new SimilarityResponse(project_id, similarities));
    }
}
