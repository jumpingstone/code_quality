package com.jumpingstone.codequality.fireeye.service.controller;


import com.jumpingstone.codequality.fireeye.service.SimilarityResponse;
import com.jumpingstone.codequality.fireeye.service.SimilarityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/similarity")
public class CodeSimilarityController {
    @Autowired
    private SimilarityService similarityService;

    @GetMapping("/{project_id}/{threshold}")
    private Mono<SimilarityResponse> getSimilaryFiles(@PathVariable String project_id, @PathVariable Float threshold) {
        return similarityService.findSimilarityFiles(project_id, threshold);
    }


    @GetMapping("/{project_id}/{file_id}/{threshold}")
    private Mono<SimilarityResponse> getSimilaryFiles(@PathVariable String project_id,
                                                      @PathVariable Integer file_id, @PathVariable Float threshold) {
        return similarityService.findSimilarityFiles(project_id, file_id, threshold);
    }
}
