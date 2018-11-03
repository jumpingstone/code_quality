package com.jumpingstone.codequality.fireeye.model;

import com.jumpingstone.codequality.fireeye.SimilarityGraphicNode;

import java.nio.file.Path;

public class Similarity {
    private final SimilarityGraphicNode fileNode;
    private final SimilarityGraphicNode otherFileNode;
    private final Float value;

    public Similarity(SimilarityGraphicNode fileNode, SimilarityGraphicNode otherNode, Float value) {
        this.fileNode = fileNode;

        this.otherFileNode = otherNode;
        this.value = value;
    }

    public Path getFile() {
        return fileNode.getFile();
    }

    public Long getFileSize() {return fileNode.getSize();}

    public Path getPeer() {
        return otherFileNode.getFile();
    }

    public Long getPeerSize() {
        return otherFileNode.getSize();
    }

    public Float getValue() {
        return value;
    }

}
