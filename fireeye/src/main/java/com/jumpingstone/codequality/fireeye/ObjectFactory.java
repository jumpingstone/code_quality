package com.jumpingstone.codequality.fireeye;

import com.jumpingstone.codequality.fireeye.cals.Aggregators;
import com.jumpingstone.codequality.fireeye.cals.SimilarityCalculatorManager;
import com.jumpingstone.codequality.fireeye.cals.lcs.LCSCalculator;
import com.jumpingstone.codequality.fireeye.cals.pcd.PCDSimilarityCalculator;
import com.jumpingstone.codequality.fireeye.persistent.GraphicDBSimilarityService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;

/**
 * Created by chenwei on 2018/10/30.
 */
public class ObjectFactory {

    public FileSimilarityGraphic createDatabase(String graphicDBDirectory) {
        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( new File(graphicDBDirectory) );
        registerShutdownHook( graphDb );
        return new GraphicDBSimilarityService(graphDb);
    }

    public SimilarityCalculatorManager createCalculator() {
        SimilarityCalculatorManager manager = new SimilarityCalculatorManager();
        manager.addCalculator(new LCSCalculator());
        manager.addCalculator(new PCDSimilarityCalculator());
        manager.setAggregator(Aggregators.MAX);
        return manager;
    }


    private static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        } );
    }
}
