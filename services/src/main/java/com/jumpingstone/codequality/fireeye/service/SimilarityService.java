package com.jumpingstone.codequality.fireeye.service;

import com.jumpingstone.codequality.fireeye.service.controller.SimilarityStatisticResponse;
import reactor.core.publisher.Mono;

public interface SimilarityService {
    Mono<SimilarityStatisticResponse> getSimilarityStatistic(String project_id);

    Mono<SimilarityResponse> findSimilarityFiles(String project_id, Float threshold);

    Mono<SimilarityResponse> findSimilarityFiles(String project_id, Integer file_id, Float threshold);
}

