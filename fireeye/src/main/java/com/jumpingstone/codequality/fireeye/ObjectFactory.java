package com.jumpingstone.codequality.fireeye;

import com.jumpingstone.codequality.fireeye.cals.Aggregators;
import com.jumpingstone.codequality.fireeye.cals.SimilarityCalculatorManager;
import com.jumpingstone.codequality.fireeye.cals.lcs.LCSCalculator;
import com.jumpingstone.codequality.fireeye.cals.pcd.PCDSimilarityCalculator;

import java.nio.file.Paths;

/**
 * Created by chenwei on 2018/10/30.
 */
public class ObjectFactory {

    public ProjectManager createProjectManager(String dbPath) {
        return new ProjectManager(Paths.get(dbPath), this);
    }

    public SimilarityCalculatorManager createCalculator() {
        SimilarityCalculatorManager manager = new SimilarityCalculatorManager();
        manager.addCalculator(new LCSCalculator());
        manager.addCalculator(new PCDSimilarityCalculator());
        manager.setAggregator(Aggregators.MAX);
        return manager;
    }


}
