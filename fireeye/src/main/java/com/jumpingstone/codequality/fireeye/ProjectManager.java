package com.jumpingstone.codequality.fireeye;

import com.jumpingstone.codequality.fireeye.graphic.GraphicLabels;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;

import java.nio.file.Files;
import java.nio.file.Path;

public class ProjectManager {

    private final Path projectDataBasePath;
    private GraphDatabaseService projectGraphicDatabase;

    public ProjectManager(final Path databaseMainDirectory) {
        this.projectDataBasePath = databaseMainDirectory;
    }
    
    public Project createProject(Path projectPath) {
        if (Files.isDirectory(projectPath)) {
            Path dbDir = null;
            if (projectDataBasePath != null) {
                dbDir = projectDataBasePath.resolve("");
            }
            return new Project(projectPath, dbDir);
        } else{
            throw new IllegalArgumentException(projectPath.toString());
        }
    }

    private void loadProjects() {
        this.projectGraphicDatabase = new ObjectFactory().createGraphicDB(projectDataBasePath.toAbsolutePath().toString(),
                9999);
        ResourceIterator<Node> nodes = projectGraphicDatabase.findNodes(GraphicLabels.Project);
        nodes.stream().forEach( n -> {
            restoreProject(n);
        });

    }

    private void restoreProject(Node node) {

    }
}
