package com.jumpingstone.codequality.fireeye;

import com.jumpingstone.codequality.fireeye.model.IProject;
import mockit.Injectable;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.testng.Assert.*;

public class ScanTaskTest {

    private @Injectable FileSimilarityGraphic graphic;

    @Test
    public void testCalculatorEffort() {
        IProject project = new IProject() {
            @Override
            public String getName() {
                return "fireeye";
            }

            @Override
            public Path getPath() {
                return Paths.get(".");
            }
        };

        TaskProgressMonitor monitor = new TaskProgressMonitor();
        ScanTask scanTask = new ScanTask(graphic, project, monitor);

        scanTask.call();

        assertTrue(monitor.finished());
    }
}