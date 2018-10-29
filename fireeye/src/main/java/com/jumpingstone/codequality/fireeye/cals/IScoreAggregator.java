package com.jumpingstone.codequality.fireeye.cals;

import com.jumpingstone.codequality.fireeye.SimilarityCalculator;

import java.util.Map;

/**
 * Created by chenwei on 2018/10/29.
 */
public interface IScoreAggregator {

    float aggregate(Map<Class<? extends SimilarityCalculator>, Float> scores);

}
