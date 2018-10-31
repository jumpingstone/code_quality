package com.jumpingstone.codequality.fireeye;

import com.jumpingstone.codequality.fireeye.cals.SimilarityCalculatorManager;
import org.neo4j.ogm.annotation.NodeEntity;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

@NodeEntity
public class Project {
    private final Path projectPath;
    private final Path dbDir;
    private String name;

    public Project(Path projectPath, Path dbDir) {
        this.projectPath = projectPath;
        this.dbDir = dbDir;
        this.name = projectPath.getFileName().toString();
    }

    public String getName() {
        return name;
    }

    public Path getPath() {
        return projectPath;
    }

    public void scan() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ObjectFactory objectFactory = new ObjectFactory();
                FileSimilarityGraphic graphicDB = objectFactory.createDatabase(dbDir.toAbsolutePath().toString());
                final SimilarityCalculatorManager calculatorManager = objectFactory.createCalculator();
                Path sourceDir = null;
                try {
                    sourceDir = findSourceDir(projectPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            if (file.endsWith(".java")) {
                                calculatorManager.updateSimilarity(graphicDB, file);
                            }
                            return super.visitFile(file, attrs);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Path findSourceDir(Path projectPath) throws IOException {
        Stream<Path> pathStream =
                Files.find(projectPath, 4, (path, attr)-> path.endsWith("src"), FileVisitOption.FOLLOW_LINKS);
        Optional<Path> optional = pathStream.findFirst();
        return optional.isPresent()?optional.get(): null;
    }
}
