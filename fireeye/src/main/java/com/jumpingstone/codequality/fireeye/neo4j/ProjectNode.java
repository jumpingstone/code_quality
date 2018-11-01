package com.jumpingstone.codequality.fireeye.neo4j;

import com.jumpingstone.codequality.fireeye.graphic.PropertyNames;
import com.jumpingstone.codequality.fireeye.model.IProject;
import org.neo4j.graphdb.Node;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by chenwei on 2018/10/31.
 */
public class ProjectNode implements IProject {

    private final Node node;

    public ProjectNode(Node node) {
        this.node = node;
    }

    @Override
    public String getName() {
        return (String) node.getProperty(PropertyNames.PROJECT_NAME);
    }

    @Override
    public Path getPath() {
        String path = (String) node.getProperty(PropertyNames.PATH);
        return Paths.get(path);
    }
}
