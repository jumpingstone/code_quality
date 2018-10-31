package com.jumpingstone.codequality.fireeye.cals;

import com.jumpingstone.codequality.fireeye.FileSimilarityGraphic;
import com.jumpingstone.codequality.fireeye.SimilarityCalculator;
import com.jumpingstone.codequality.fireeye.SimilarityGraphicNode;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * Created by chenwei on 2018/10/28.
 */
public class SimilarityCalculatorManager {

    private List<SimilarityCalculator> calculatorList = new ArrayList<>();
    private IScoreAggregator aggregator = Aggregators.AVG;

    public void addCalculator(SimilarityCalculator calculator) {
        calculatorList.add(calculator);
    }

    public void setAggregator(IScoreAggregator aggregator) {
        this.aggregator = aggregator;
    }

    public void updateSimilarity(FileSimilarityGraphic network, Path file) throws IOException {
        Set<Path> visitedMap = new HashSet<>();
        SimilarityGraphicNode newNode = network.createNode(file);
        visitedMap.add(file);

        Iterator<SimilarityGraphicNode> nodeIterator = network.getNodes();
        visit(file, newNode, network, visitedMap, nodeIterator);
    }

    private void visit(Path file, SimilarityGraphicNode newNode,
                       FileSimilarityGraphic network,
                       Set<Path> visitedMap,
                       Iterator<SimilarityGraphicNode> nodeIterator) {
        while(nodeIterator.hasNext()) {
            SimilarityGraphicNode node = nodeIterator.next();
            Path fileToCompare = node.getFile();
            if (!visitedMap.contains(fileToCompare)) {
                visitedMap.add(fileToCompare);

                float similarity = calculate(fileToCompare, file);
                if (similarity > 0.3) {
                    network.updateSimilarity(newNode, node, similarity);

                    Iterator<SimilarityGraphicNode> similarNodes = node.getSimilarNodes(0.8f);
                    visit(file, newNode, network, visitedMap, similarNodes);
                }
            }
        }
    }

    private float calculate(Path fileToCompare, Path file) {
        //pre calculate
        Map<Class<? extends SimilarityCalculator>, Float> scores = new HashMap<>();
        calculatorList.parallelStream().forEach( c -> {
            float score = 0;
            try {
                score = c.calculate(file, fileToCompare);
            } catch (IOException e) {
                e.printStackTrace();
            }
            scores.put(c.getClass(), score);
        });

        return aggregator.aggregate(scores);
    }

}
