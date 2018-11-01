package com.jumpingstone.codequality.fireeye;

import com.jumpingstone.codequality.fireeye.cals.SimilarityCalculatorManager;
import com.jumpingstone.codequality.fireeye.model.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by chenwei on 2018/10/31.
 */
public class ScanTask implements Callable<Void> {

    private final IProject project;
    private final FileSimilarityGraphic graphicDB;
    private final IProgressMonitor progressMonitor;

    public ScanTask(FileSimilarityGraphic graphic, IProject project, IProgressMonitor monitor) {
        this.graphicDB = graphic;
        this.project = project;
        this.progressMonitor = monitor;
    }

    public IProject getProject() {
        return project;
    }

    public IProgressMonitor getProgressMonitor() {
        return progressMonitor;
    }

    @Override
    public Void call() {
        ObjectFactory objectFactory = new ObjectFactory();
        final SimilarityCalculatorManager calculatorManager = objectFactory.createCalculator();
        Collection<Path> sourceDirs = null;
        Map<Path, Integer> effort = null;
        try {
            sourceDirs = findSourceDir(project.getPath());
            effort = calculatorEffort(sourceDirs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long totalWork = effort.values().stream().collect(Collectors.summingLong(
                i -> (Long) i.longValue()
        ));
        progressMonitor.beginTask("find source directory", (int) totalWork);
        try {
            for(Path sourceDir : sourceDirs) {
                final SubMonitor monitor = SubMonitor.convert(progressMonitor, "scan files in " + sourceDir,
                        effort.get(sourceDir));
                Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        if (file.endsWith(".java")) {
                            monitor.setTaskName("analyze file: " + file.toString());
                            calculatorManager.updateSimilarity(graphicDB, file);
                            monitor.worked(1);
                        }
                        return super.visitFile(file, attrs);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        progressMonitor.done();
        return null;
    }

    private Map<Path,Integer> calculatorEffort(Collection<Path> sourceDirs) throws IOException {
        Map<Path,Integer> countmap = new HashMap<>();
        for(Path path : sourceDirs) {
            long count = Files.find(path, 20, (file, attr) -> Files.isRegularFile(file)
                && file.toString().endsWith(".java"), FileVisitOption.FOLLOW_LINKS).count();
            countmap.put(path, (int)count);
        }
        return countmap;
    }

    private Collection<Path> findSourceDir(Path projectPath) throws IOException {
        Stream<Path> pathStream =
                Files.find(projectPath, 4, (path, attr)-> Files.isDirectory(path)
                        && path.endsWith("src")
                        && !path.toAbsolutePath().toString().contains(File.separator + "test"),
                        FileVisitOption.FOLLOW_LINKS);

        return pathStream.collect(Collectors.toCollection(HashSet::new));
    }
}
