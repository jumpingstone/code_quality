package com.jumpingstone.codequality.fireeye.model;

import com.jumpingstone.codequality.fireeye.TaskProgressMonitor;

import java.nio.file.Path;

public class ManagedProject implements IProject {
    private final IProject project;
    private final TaskProgressMonitor monitor;

    public ManagedProject(IProject project, TaskProgressMonitor monitor) {
        this.project = project;
        this.monitor = monitor;
    }

    @Override
    public String getName() {
        return project.getName();
    }

    @Override
    public Path getPath() {
        return project.getPath();
    }

    public ProjectStatus getStatus() {
        ProjectStatus status =  new ProjectStatus(ScanStatus.NotStarted, 0);
        if (monitor != null) {
            if (monitor.isCanceled()) {
                status = new ProjectStatus(ScanStatus.Canceled, monitor.getPercentage());
            } else if (monitor.finished()){
                status = new ProjectStatus(ScanStatus.Finished, 100);
            } else {
                status = new ProjectStatus(ScanStatus.OnProgress, monitor.getPercentage());
            }
        }
        return status;
    }

    public class ProjectStatus {
        private final ScanStatus status;
        private final int percentage;

        public ProjectStatus(ScanStatus status, int percentage) {
            this.status = status;
            this.percentage = percentage;
        }

        public ScanStatus getStatus() {
            return status;
        }

        public int getPercentage() {
            return percentage;
        }
    }

    enum ScanStatus {
        NotStarted,
        OnProgress,
        Finished,
        Canceled
    }
}
