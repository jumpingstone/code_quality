package com.jumpingstone.codequality.fireeye.service.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenwei on 2018/11/17.
 */
public class SimilarityStatisticResponse {

    private final Map<Float, Integer> bulkets;
    private final String project;

    public SimilarityStatisticResponse(String project_id, Map<Float, Integer> similarities) {
        this.project = project_id;
        this.bulkets = new HashMap<>(similarities);
    }

    public Map<Float, Integer> getBulkets() {
        return Collections.unmodifiableMap(this.bulkets);
    }

}
