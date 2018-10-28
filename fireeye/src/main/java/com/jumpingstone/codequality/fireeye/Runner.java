package com.jumpingstone.codequality.fireeye;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Runner {
    Logger logger = LoggerFactory.getLogger(Runner.class);

    public static class Scanner {
        public void scan(Path filePath) throws IOException {
            graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( databaseDirectory );
            registerShutdownHook( graphDb );

            Files.walkFileTree(filePath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.endsWith(".java")) {
                        createNode(file);
                    }
                    return super.visitFile(file, attrs);
                }
            });
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

        private void createNode(Path file) {
            try {
                AtomicInteger linenumber = new AtomicInteger(0);
                Files.lines(file).forEach( l -> {
                    new TextLineLCS.TextLine(linenumber.get(), l);
                    linenumber.incrementAndGet();
                });

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
