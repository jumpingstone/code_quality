package com.jumpingstone.codequality.fireeye.cals;

import com.jumpingstone.codequality.fireeye.FileSimilarityGraphic;
import com.jumpingstone.codequality.fireeye.SimilarityGraphicNode;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * Created by chenwei on 2018/10/28.
 */
public class SimilarityCalculatorManager {

    public void updateSimilarity(FileSimilarityGraphic network, Path file) throws IOException {
        Set<Path> visitedMap = new HashSet<>();
        Iterator<SimilarityGraphicNode> nodeIterator = network.getNodes();
        visit(file, visitedMap, nodeIterator);
    }

    private void visit(Path file, Set<Path> visitedMap, Iterator<SimilarityGraphicNode> nodeIterator) {
        while(nodeIterator.hasNext()) {
            SimilarityGraphicNode node = nodeIterator.next();
            Path fileToCompare = node.getFile();
            if (!visitedMap.contains(fileToCompare)) {
                visitedMap.add(fileToCompare);
                float similarity = calculate(fileToCompare, file);
                if (similarity > 0.3) {
                    Iterator<SimilarityGraphicNode> similarNodes = node.getSimilarNodes(0.8f);
                    visit(file, visitedMap, similarNodes);
                }
            }
        }
    }

    private float calculate(Path fileToCompare, Path file) {
        return 0;
    }

}
