package com.jumpingstone.codequality.fireeye.neo4j;

import com.jumpingstone.codequality.fireeye.FileSimilarityGraphic;
import com.jumpingstone.codequality.fireeye.SimilarityGraphicNode;
import com.jumpingstone.codequality.fireeye.graphic.GraphicLabels;
import com.jumpingstone.codequality.fireeye.graphic.PropertyNames;
import com.jumpingstone.codequality.fireeye.model.IProject;
import org.neo4j.graphdb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by chenwei on 2018/10/30.
 */
public class GraphicDBSimilarityService implements FileSimilarityGraphic {
    private final Logger logger = LoggerFactory.getLogger(GraphicDBSimilarityService.class);
    private final GraphDatabaseService graphicDB;
    private final IProject project;

    public GraphicDBSimilarityService(IProject project, GraphDatabaseService graphDb) {
        this.project = project;
        this.graphicDB = graphDb;
    }

    @Override
    public SimilarityGraphicNode createNode(Path file) {
        SimilarityGraphicNode fileNode = getNode(file);
        if (fileNode == null) {
            try (Transaction tx = graphicDB.beginTx()) {
                Node projectNode = graphicDB.findNode(GraphicLabels.Project, PropertyNames.PROJECT_NAME,
                        project.getName());
                Node node = graphicDB.createNode(GraphicLabels.Java_File);
                node.setProperty(PropertyNames.PATH, file.toAbsolutePath().toString());
                node.setProperty(PropertyNames.PROJECT_NAME, project.getName());
                projectNode.createRelationshipTo(node, NodeRelationships.Contains);
                tx.success();
                fileNode = new GraphicFileNode(graphicDB, node);
            }
        }
        return fileNode;
    }

    @Override
    public SimilarityGraphicNode getNode(Path file) {
        GraphicFileNode fileNode = null;
        try (Transaction tx = graphicDB.beginTx()) {
            Node node = findInternalNode(file, fileNode);
            if (node != null) {
                fileNode = new GraphicFileNode(graphicDB, node);
            }
            tx.success();
        }
        return fileNode;
    }

    @Override
    public Iterable<SimilarityGraphicNode> getNodes() {
        try (Transaction tx = graphicDB.beginTx()) {
            ResourceIterator<Node> nodeIterator = graphicDB.findNodes(GraphicLabels.Java_File,
                    PropertyNames.PROJECT_NAME, project.getName());
            List<SimilarityGraphicNode> nodes = nodeIterator.stream().map(n -> new GraphicFileNode(graphicDB, n)).
                    collect(Collectors.toList());
            tx.success();
            return nodes;
        }
    }

    @Override
    public void updateSimilarity(SimilarityGraphicNode newNode, SimilarityGraphicNode node, float similarity) {
        try (Transaction tx = graphicDB.beginTx()) {
            GraphicFileNode gNode = (GraphicFileNode) node;
            GraphicFileNode newGNode = (GraphicFileNode) newNode;
            Relationship relationship = null;
            for(Relationship r :
                    gNode.getNode().getRelationships(Direction.BOTH, NodeRelationships.Similar)) {
                if (r.getOtherNode(gNode.getNode()).equals(newGNode)) {
                    relationship = r;
                    break;
                }
            }
            if (relationship == null) {
                relationship = newGNode.getNode().createRelationshipTo(gNode.getNode(),
                        NodeRelationships.Similar);
            }
            relationship.setProperty(PropertyNames.SIMILARITY, similarity);
            tx.success();
        }
    }



    private Node findInternalNode(Path file, GraphicFileNode fileNode) {
        Node node = null;
        ResourceIterator<Node> nodes = graphicDB.findNodes(GraphicLabels.Java_File,
                PropertyNames.PROJECT_NAME, project.getName(),
                PropertyNames.PATH, file.toAbsolutePath().toString());
        if (nodes != null && nodes.hasNext()) {
            node = nodes.next();
        }
        return node;
    }

}
