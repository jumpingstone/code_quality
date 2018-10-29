package com.jumpingstone.codequality.fireeye.neo4j;

import com.jumpingstone.codequality.fireeye.FileSimilarityGraphic;
import com.jumpingstone.codequality.fireeye.SimilarityGraphicNode;

import java.nio.file.Path;

/**
 * Created by chenwei on 2018/10/28.
 */
public class Neo4JGraphicAdapter implements FileSimilarityGraphic {
    @Override
    public SimilarityGraphicNode getNode(Path file) {
        return null;
    }
}
