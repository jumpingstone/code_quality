package com.jumpingstone.codequality.fireeye;

import java.nio.file.Path;
import java.util.Iterator;

/**
 * Created by chenwei on 2018/10/28.
 */
public interface FileSimilarityGraphic {

    SimilarityGraphicNode createNode(Path file);

    SimilarityGraphicNode getNode(Path file);

    Iterable<SimilarityGraphicNode> getNodes();

    void updateSimilarity(SimilarityGraphicNode newNode, SimilarityGraphicNode node, float similarity);
}
