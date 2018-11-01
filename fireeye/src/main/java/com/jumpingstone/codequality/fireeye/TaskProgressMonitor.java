package com.jumpingstone.codequality.fireeye;

import org.eclipse.core.runtime.IProgressMonitor;

import java.util.LinkedList;

public class TaskProgressMonitor implements IProgressMonitor {
    private static final int CAPACITY = 1000;
    private LinkedList<String> content = new LinkedList();
    private double totalWork = 10000000;
    private double doneWork = 0;
    private boolean cancel = false;
    private boolean isDone = false;

    @Override
    public void beginTask(String msg, int totalWork) {
        appendContent(msg);
        this.totalWork = totalWork;
    }

    @Override
    public void done() {
        doneWork = totalWork;
        isDone = true;
    }

    public boolean finished() {
        return isDone;
    }

    @Override
    public void internalWorked(double v) {

    }

    @Override
    public boolean isCanceled() {
        return cancel;
    }

    @Override
    public void setCanceled(boolean b) {
        cancel = b;
    }

    @Override
    public void setTaskName(String submsg) {
        appendContent(submsg);
    }

    @Override
    public void subTask(String submsg) {
        appendContent(submsg);
    }

    @Override
    public void worked(int i) {
        doneWork += i;
        if (doneWork > totalWork) {
            done();
        }
    }

    public int getPercentage() {
        return (int) (100 * doneWork / totalWork);
    }

    public String getContent() {
        return content.toString();
    }

    private void appendContent(String msg) {
        content.add(msg);
        if (content.size() > CAPACITY) {
            content.removeFirst();
        }
        //TODO: notify
    }
}
