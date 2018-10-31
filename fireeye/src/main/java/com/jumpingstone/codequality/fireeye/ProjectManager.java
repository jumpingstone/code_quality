package com.jumpingstone.codequality.fireeye;

import com.jumpingstone.codequality.fireeye.graphic.GraphicLabels;
import com.jumpingstone.codequality.fireeye.graphic.PropertyNames;
import com.jumpingstone.codequality.fireeye.model.IProject;
import com.jumpingstone.codequality.fireeye.neo4j.ProjectNode;
import com.jumpingstone.codequality.fireeye.neo4j.GraphicDBSimilarityService;
import org.hamcrest.Matcher;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.configuration.BoltConnector;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class ProjectManager {

    private final Path projectDataBasePath;
    private final ExecutorService executor = Executors.newFixedThreadPool(2);
    private final ConcurrentHashMap<IProject, Future<Void>> taskMap = new ConcurrentHashMap();
    private final ObjectFactory objectFactory;
    private GraphDatabaseService projectGraphicDatabase;
    private GraphicDBSimilarityService similarityService;

    public ProjectManager(final Path databaseMainDirectory, final ObjectFactory objectFactory) {
        this.projectDataBasePath = databaseMainDirectory;
        this.objectFactory = objectFactory;
        this.projectGraphicDatabase = createGraphicDB(projectDataBasePath.toAbsolutePath().toString(),
                7678);
        this.similarityService = new GraphicDBSimilarityService(projectGraphicDatabase);
    }

    public IProject getProject(String project_id) {
        Node node = projectGraphicDatabase.findNode(GraphicLabels.Project, PropertyNames.PROJECT_NAME,
                project_id);
        return node== null? null : new ProjectNode(node);
    }


    public List<IProject> findProject(Matcher<IProject> matcher) {
        ResourceIterator<Node> nodes = projectGraphicDatabase.findNodes(GraphicLabels.Project);
        return nodes.stream().filter(node -> matcher.matches(node)).map(
                node -> (IProject)(new ProjectNode(node))).collect(Collectors.toList());
    }

    public IProject getProjectByPath(String path) {
        Node node = projectGraphicDatabase.findNode(GraphicLabels.Project, PropertyNames.PATH,
                path);
        return node== null? null : new ProjectNode(node);
    }

    public IProject createProject(String name, Path projectPath) {
        IProject project = null;
        if (name == null) {
            name = projectPath.getFileName().toString();
        }
        if (Files.isDirectory(projectPath)) {
            try (Transaction tx = projectGraphicDatabase.beginTx()) {
                 if (projectGraphicDatabase.findNode(GraphicLabels.Project, PropertyNames.PROJECT_NAME, name) == null
                         && projectGraphicDatabase.findNode(GraphicLabels.Project, PropertyNames.PATH, projectPath) == null ) {
                     Node node = projectGraphicDatabase.createNode(GraphicLabels.Project);
                     node.setProperty(PropertyNames.PROJECT_NAME, name);
                     node.setProperty(PropertyNames.PATH, projectPath);
                     project = new ProjectNode(node);
                 } else {
                     throw new IllegalArgumentException("project already exist");
                 }
            }
            return project;
        } else{
            throw new IllegalArgumentException(projectPath.toString());
        }
    }

    public IProject scanProject(String name) {
        IProject project = getProject(name);
        if (project != null) {
            //check if we have the scan project task in queue.
            if (!taskMap.contains(project)) {
                taskMap.put(project,
                        executor.submit(new ScanTask(similarityService, project)));
            }
        }
        return project;
    }

    private void restoreProject(Node node) {

    }

    private GraphDatabaseService createGraphicDB(String graphicDBDirectory, int port) {
        BoltConnector bolt = new BoltConnector( "0" );
        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder( new File(graphicDBDirectory) )
                .setConfig( bolt.type, "BOLT" )
                .setConfig( bolt.enabled, "true" )
                .setConfig( bolt.listen_address, "localhost:" + port )
                .newGraphDatabase();
        registerShutdownHook( graphDb );
        return graphDb;
    }

    private static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        } );
    }
}
