package com.jumpingstone.codequality.fireeye.service;

import reactor.core.publisher.Mono;

public interface SimilarityService {
    Mono<SimilarityResponse> findSimilarityFiles(String project_id, Float threshold);

    Mono<SimilarityResponse> findSimilarityFiles(String project_id, Integer file_id, Float threshold);
}

