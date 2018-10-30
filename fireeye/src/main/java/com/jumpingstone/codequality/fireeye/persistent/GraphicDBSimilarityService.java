package com.jumpingstone.codequality.fireeye.persistent;

import com.jumpingstone.codequality.fireeye.FileSimilarityGraphic;
import com.jumpingstone.codequality.fireeye.SimilarityGraphicNode;
import com.jumpingstone.codequality.fireeye.graphic.GraphicLabels;
import com.jumpingstone.codequality.fireeye.graphic.PropertyNames;
import org.neo4j.graphdb.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

/**
 * Created by chenwei on 2018/10/30.
 */
public class GraphicDBSimilarityService implements FileSimilarityGraphic {
    private final GraphDatabaseService graphicDB;

    public GraphicDBSimilarityService(GraphDatabaseService graphDb) {
        this.graphicDB = graphDb;
    }

    @Override
    public SimilarityGraphicNode createNode(Path file) {
        Node node = graphicDB.createNode(GraphicLabels.Java_File);
        node.setProperty(PropertyNames.FILE_NAME, file.getFileName());
        return new GraphicFileNode(node);
    }

    @Override
    public SimilarityGraphicNode getNode(Path file) {
        GraphicFileNode fileNode = null;
        Node node = graphicDB.findNode(GraphicLabels.Java_File, PropertyNames.FILE_NAME, file.getFileName());
        if (node != null) {
            fileNode = new GraphicFileNode(node);
        }
        return fileNode;
    }

    @Override
    public Iterator<SimilarityGraphicNode> getNodes() {
        ResourceIterator<Node> nodeIterator = graphicDB.getAllNodes().iterator();
        return new Iterator<SimilarityGraphicNode>() {
            @Override
            public boolean hasNext() {
                return nodeIterator.hasNext();
            }

            @Override
            public SimilarityGraphicNode next() {
                return new GraphicFileNode(nodeIterator.next());
            }
        };
    }

    @Override
    public void updateSimilarity(SimilarityGraphicNode newNode, SimilarityGraphicNode node, float similarity) {
        GraphicFileNode otherNode = (GraphicFileNode)node;
        Relationship relationship = ((GraphicFileNode)newNode).node.createRelationshipTo(otherNode.node,
                NodeRelationships.Similar);
        relationship.setProperty(PropertyNames.SIMILARITY, similarity);
    }

    public class GraphicFileNode implements SimilarityGraphicNode {
        private final Node node;

        public GraphicFileNode(Node node) {
            this.node = node;
        }

        @Override
        public Iterator<SimilarityGraphicNode> getSimilarNodes(float threshold) {
            return null;
        }

        @Override
        public Path getFile() {
            return (Path) node.getProperty(PropertyNames.FILE_NAME);
        }
    }

    enum NodeRelationships implements RelationshipType {
        Similar
    }
}
