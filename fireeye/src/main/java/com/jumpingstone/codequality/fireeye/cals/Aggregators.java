package com.jumpingstone.codequality.fireeye.cals;

import com.jumpingstone.codequality.fireeye.SimilarityCalculator;

import java.util.Map;
import java.util.OptionalDouble;

/**
 * Created by chenwei on 2018/10/29.
 */
public class Aggregators {
    public static final IScoreAggregator AVG = new IScoreAggregator() {
        @Override
        public float aggregate(Map<Class<? extends SimilarityCalculator>, Float> scores) {
            return (float) scores.values().stream().mapToDouble(value -> value.doubleValue()).sum();
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    };

    public static final IScoreAggregator MAX = new IScoreAggregator() {
        @Override
        public float aggregate(Map<Class<? extends SimilarityCalculator>, Float> scores) {
            OptionalDouble v =scores.values().stream().mapToDouble(value -> value.doubleValue()).max();
            if (v.isPresent()) {
                return (float)v.getAsDouble();
            }
            return 0;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    };
}
