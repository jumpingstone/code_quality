package com.jumpingstone.codequality.fireeye.neo4j;

import com.jumpingstone.codequality.fireeye.SimilarityGraphicNode;
import com.jumpingstone.codequality.fireeye.graphic.PropertyNames;
import org.neo4j.graphdb.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by chenwei on 2018/10/31.
 */
public class GraphicFileNode implements SimilarityGraphicNode {

    private final Node node;
    private final Path file;
    private final GraphDatabaseService db;


    public GraphicFileNode(GraphDatabaseService graphicDB, Node node) {
        this.db = graphicDB;
        this.node = node;
        this.file = Paths.get((String) node.getProperty(PropertyNames.PATH));
    }

    @Override
    public Iterable<SimilarityGraphicNode> getSimilarNodes(float threshold) {
        Set<SimilarityGraphicNode> nodeList = new HashSet<>();

        try (Transaction tx = db.beginTx()) {
            Iterable<Relationship> relationships =
                    node.getRelationships(NodeRelationships.Similar, Direction.BOTH);

            for (Relationship relationship : relationships) {
                if ((Float) (relationship.getProperty(PropertyNames.SIMILARITY)) > threshold) {
                    nodeList.add(new GraphicFileNode(db, relationship.getEndNode()));
                }
            }
            tx.success();
        }

        return nodeList;
    }

    @Override
    public Path getFile() {
        return file;
    }

    Node getNode() {
        return node;
    }

}
