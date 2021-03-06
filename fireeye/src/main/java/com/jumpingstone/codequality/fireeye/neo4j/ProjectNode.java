package com.jumpingstone.codequality.fireeye.neo4j;

import com.jumpingstone.codequality.fireeye.graphic.PropertyNames;
import com.jumpingstone.codequality.fireeye.model.IProject;
import org.neo4j.graphdb.Node;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Created by chenwei on 2018/10/31.
 */
public class ProjectNode implements IProject {

    private final Node node;
    private final String name;
    private final Path path;

    public ProjectNode(Node node) {
        this.node = node;
        name = (String) node.getProperty(PropertyNames.PROJECT_NAME);
        path = Paths.get((String) node.getProperty(PropertyNames.PATH));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectNode that = (ProjectNode) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, path);
    }
}
