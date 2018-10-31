package com.jumpingstone.codequality.fireeye;

import com.jumpingstone.codequality.fireeye.cals.SimilarityCalculatorManager;
import com.jumpingstone.codequality.fireeye.model.IProject;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

/**
 * Created by chenwei on 2018/10/31.
 */
public class ScanTask implements Callable<Void> {

    private final IProject project;
    private final FileSimilarityGraphic graphicDB;

    public ScanTask(FileSimilarityGraphic graphic, IProject project) {
        this.graphicDB = graphic;
        this.project = project;
    }

    @Override
    public Void call() throws Exception {
        ObjectFactory objectFactory = new ObjectFactory();
        final SimilarityCalculatorManager calculatorManager = objectFactory.createCalculator();
        Path sourceDir = null;
        try {
            sourceDir = findSourceDir(project.getPath());
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
        return null;
    }


    private Path findSourceDir(Path projectPath) throws IOException {
        Stream<Path> pathStream =
                Files.find(projectPath, 4, (path, attr)-> path.endsWith("src"), FileVisitOption.FOLLOW_LINKS);
        Optional<Path> optional = pathStream.findFirst();
        return optional.isPresent()?optional.get(): null;
    }
}
