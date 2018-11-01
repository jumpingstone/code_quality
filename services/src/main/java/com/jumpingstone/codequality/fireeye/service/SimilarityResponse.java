package com.jumpingstone.codequality.fireeye.service;

import com.jumpingstone.codequality.fireeye.model.Similarity;

import java.util.Set;

public class SimilarityResponse {
    private final String projectName;
    private final Set<Similarity> similarityFiles;

    public SimilarityResponse(String project_id, Set<Similarity> similarities) {
        this.projectName = project_id;
        this.similarityFiles = similarities;
    }

    public String getProjectName() {
        return projectName;
    }

    public Set<Similarity> getSimilarityFiles() {
        return similarityFiles;
    }
}
