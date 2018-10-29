package com.jumpingstone.codequality.fireeye;

import java.nio.file.Path;
import java.util.Iterator;

/**
 * Created by chenwei on 2018/10/28.
 */
public interface SimilarityGraphicNode {

    Iterator<SimilarityGraphicNode> getSimilarNodes(float threshold);

    Path getFile();
}
