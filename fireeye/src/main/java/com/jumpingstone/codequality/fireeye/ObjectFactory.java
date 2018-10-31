package com.jumpingstone.codequality.fireeye;

import com.jumpingstone.codequality.fireeye.cals.Aggregators;
import com.jumpingstone.codequality.fireeye.cals.SimilarityCalculatorManager;
import com.jumpingstone.codequality.fireeye.cals.lcs.LCSCalculator;
import com.jumpingstone.codequality.fireeye.cals.pcd.PCDSimilarityCalculator;
import com.jumpingstone.codequality.fireeye.persistent.GraphicDBSimilarityService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.kernel.configuration.BoltConnector;

import java.io.File;

/**
 * Created by chenwei on 2018/10/30.
 */
public class ObjectFactory {

    public FileSimilarityGraphic createDatabase(String graphicDBDirectory) {
        int port = 7678;
        GraphDatabaseService graphDb = createGraphicDB(graphicDBDirectory, port);
        return new GraphicDBSimilarityService(graphDb);
    }

    public GraphDatabaseService createGraphicDB(String graphicDBDirectory, int port) {
        BoltConnector bolt = new BoltConnector( "0" );
        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder( new File(graphicDBDirectory) )
                .setConfig( bolt.type, "BOLT" )
                .setConfig( bolt.enabled, "true" )
                .setConfig( bolt.listen_address, "localhost:" + port )
                .newGraphDatabase();
        registerShutdownHook( graphDb );
        return graphDb;
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
