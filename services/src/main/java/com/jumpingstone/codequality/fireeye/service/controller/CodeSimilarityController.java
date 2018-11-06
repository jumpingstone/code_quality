package com.jumpingstone.codequality.fireeye.service.controller;


import com.jumpingstone.codequality.fireeye.service.SimilarityResponse;
import com.jumpingstone.codequality.fireeye.service.SimilarityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://10.58.91.167:3000"
}, methods={RequestMethod.GET, RequestMethod.POST})
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
