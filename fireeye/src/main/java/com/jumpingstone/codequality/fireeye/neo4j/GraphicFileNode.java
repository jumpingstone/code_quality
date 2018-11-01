package com.jumpingstone.codequality.fireeye.neo4j;

import com.jumpingstone.codequality.fireeye.SimilarityGraphicNode;
import com.jumpingstone.codequality.fireeye.graphic.PropertyNames;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by chenwei on 2018/10/31.
 */
public class GraphicFileNode implements SimilarityGraphicNode {

    private final Node node;

    public GraphicFileNode(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    @Override
    public Iterable<SimilarityGraphicNode> getSimilarNodes(float threshold) {
        Iterable<Relationship> relationships =
                node.getRelationships(NodeRelationships.Similar, Direction.BOTH);
        Set<SimilarityGraphicNode> nodeList = new HashSet<>();

        for (Relationship relationship:relationships) {
            if ((Float)(relationship.getProperty(PropertyNames.SIMILARITY)) > threshold) {
                nodeList.add(new GraphicFileNode(relationship.getEndNode()));
            }
        }
        return nodeList;
    }

    @Override
    public Path getFile() {
        return (Path) node.getProperty(PropertyNames.PATH);
    }
}
