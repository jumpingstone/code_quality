package com.jumpingstone.codequality.fireeye;

import com.jumpingstone.codequality.fireeye.cals.lcs.TextLineLCS;
import com.jumpingstone.codequality.fireeye.graphic.GraphicLabels;
import com.jumpingstone.codequality.fireeye.graphic.PropertyNames;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

public class Runner {
    Logger logger = LoggerFactory.getLogger(Runner.class);
    private final static String databaseDirectory = "./graphic_db";

    public static class Scanner {
        public void scan(Path filePath) throws IOException {
            GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( new File(databaseDirectory) );
            registerShutdownHook( graphDb );

            Files.walkFileTree(filePath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.endsWith(".java")) {
                        Node node = findNode(graphDb, file);
                        if (node == null) {
                            node = createNode(graphDb, file);
                            updateSimilarity(node, graphDb);
                        }
                    }
                    return super.visitFile(file, attrs);
                }
            });
        }

        private Node findNode(GraphDatabaseService graphDb, Path file) {
            return graphDb.findNode(GraphicLabels.Java_File, PropertyNames.FILE_NAME, file.getFileName());
        }

        private void updateSimilarity(Node node, GraphDatabaseService graphDb) {

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

        private Node createNode(GraphDatabaseService graphDb, Path file) {
            Node fileNode = null;
            try {
                AtomicInteger linenumber = new AtomicInteger(0);
                Files.lines(file).forEach( l -> {
                    new TextLineLCS.TextLine(linenumber.get(), l);
                    linenumber.incrementAndGet();
                });
                fileNode = graphDb.createNode(GraphicLabels.Java_File);
                fileNode.setProperty(PropertyNames.FILE_NAME, file.getFileName());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return fileNode;
        }
    }

}
