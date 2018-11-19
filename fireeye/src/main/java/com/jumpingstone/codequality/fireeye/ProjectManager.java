package com.jumpingstone.codequality.fireeye;

import com.jumpingstone.codequality.fireeye.graphic.GraphicLabels;
import com.jumpingstone.codequality.fireeye.graphic.PropertyNames;
import com.jumpingstone.codequality.fireeye.model.IProject;
import com.jumpingstone.codequality.fireeye.model.ManagedProject;
import com.jumpingstone.codequality.fireeye.model.Similarity;
import com.jumpingstone.codequality.fireeye.neo4j.GraphicFileNode;
import com.jumpingstone.codequality.fireeye.neo4j.NodeRelationships;
import com.jumpingstone.codequality.fireeye.neo4j.ProjectNode;
import com.jumpingstone.codequality.fireeye.neo4j.GraphicDBSimilarityService;
import org.hamcrest.Matcher;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.configuration.BoltConnector;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ProjectManager {

    private final Path projectDataBasePath;
    private final ExecutorService executor = Executors.newFixedThreadPool(2);
    private final ConcurrentHashMap<IProject, TaskProgressMonitor> taskMap = new ConcurrentHashMap();
    private final ObjectFactory objectFactory;
    private final Path codeBasePath;
    private GraphDatabaseService projectGraphicDatabase;

    public ProjectManager(final Path databaseMainDirectory, final Path codeBasePath,
                          final ObjectFactory objectFactory) {
        this.projectDataBasePath = databaseMainDirectory;
        this.codeBasePath = codeBasePath;
        this.objectFactory = objectFactory;
        this.projectGraphicDatabase = createGraphicDB(projectDataBasePath.toAbsolutePath().toString(),
                7678);
    }

    public IProject getProject(String project_id) {
        try (Transaction tx = projectGraphicDatabase.beginTx()) {
            Node node = projectGraphicDatabase.findNode(GraphicLabels.Project, PropertyNames.PROJECT_NAME,
                    project_id);
            ProjectNode projectNode = node == null ? null : new ProjectNode(node);
            tx.success();
            return projectNode;
        }
    }

    public List<IProject> findProject(Matcher<IProject> matcher) {
        try (Transaction tx = projectGraphicDatabase.beginTx()){
            ResourceIterator<Node> nodes = projectGraphicDatabase.findNodes(GraphicLabels.Project);
            List<IProject> projects = nodes.stream().filter(node -> matcher.matches(node)).map(
                node -> (IProject) (new ProjectNode(node))).collect(Collectors.toList());
            tx.success();
            return projects;
        }
    }

    public Map<Float,Integer> getSimilarityStatistic(String project_id) {

    },

    public Set<Similarity> findSimilarityFiles(String project_id, Float threshold) {
        Set<Similarity> similarities = new HashSet<>();
        try (Transaction tx = projectGraphicDatabase.beginTx()){
            Node projectNode = projectGraphicDatabase.findNode(GraphicLabels.Project, PropertyNames.PROJECT_NAME,
                    project_id);
            if (projectNode != null) {

                Map<String, Object> params = new HashMap<>();
                params.put("threshold", threshold);
                params.put("project_id", project_id);
                Result result = projectGraphicDatabase.execute("MATCH (n:Java_File)-[s:Similar]->(m:Java_File) " +
                        "WHERE s.Similarity > $threshold "  +
                        "  AND n.Project_Name=$project_id " +
                        "  AND m.Project_Name=$project_id " +
                        "RETURN n,m,s.Similarity",
                        params
                );
                while ( result.hasNext() )
                {
                    Map<String,Object> row = result.next();
                    Node fileNode = (Node) row.get("n");
                    Node otherNode = (Node) row.get("m");
                    Float similarity = (Float) row.get("s.Similarity");
                    similarities.add(new Similarity(
                            new GraphicFileNode(projectGraphicDatabase, fileNode),
                            new GraphicFileNode(projectGraphicDatabase, otherNode),
                            similarity));
                }
//
//                for(Relationship r : projectNode.getRelationships(NodeRelationships.Contains) ){
//                    Node fileNode = r.getOtherNode(projectNode);
//                    searchFileSimilarity(threshold, similarities, fileNode);
//                }

            }
            tx.success();
        }
        return similarities;
    }

    public Set<Similarity> findSimilarityFiles(String project_id, Integer file_id, Float threshold) {
        Set<Similarity> similarities = new HashSet<>();
        try (Transaction tx = projectGraphicDatabase.beginTx()){
            ResourceIterator<Node> fileNodes = projectGraphicDatabase.findNodes(GraphicLabels.Java_File,
                            PropertyNames.PROJECT_NAME, project_id,
                            PropertyNames.PATH, file_id);
            if (fileNodes != null && fileNodes.hasNext()) {
                Node fileNode = fileNodes.next();
                searchFileSimilarity(threshold, similarities, fileNode);
            }
            tx.success();
        }
        return similarities;
    }

    public IProject getProjectByPath(String path) {
        try (Transaction tx = projectGraphicDatabase.beginTx()) {
            Node node = projectGraphicDatabase.findNode(GraphicLabels.Project, PropertyNames.PATH,
                    path);
            ProjectNode projectNode = node == null ? null : new ProjectNode(node);
            tx.success();
            return projectNode;
        }
    }

    public IProject createProject(String name, Path projectPath) {
        IProject project = null;
        if (name == null) {
            name = projectPath.getFileName().toString();
        }
        if (Files.isDirectory(projectPath)) {
            try (Transaction tx = projectGraphicDatabase.beginTx()) {
                String path =  projectPath.toAbsolutePath().toString();
                 if (projectGraphicDatabase.findNode(GraphicLabels.Project, PropertyNames.PROJECT_NAME, name) == null
                         && projectGraphicDatabase.findNode(GraphicLabels.Project, PropertyNames.PATH, path) == null ) {
                     Node node = projectGraphicDatabase.createNode(GraphicLabels.Project);
                     node.setProperty(PropertyNames.PROJECT_NAME, name);
                     node.setProperty(PropertyNames.PATH, path);
                     project = new ProjectNode(node);
                     tx.success();
                 } else {
                     throw new IllegalArgumentException("project already exist");
                 }
            }
            return project;
        } else{
            throw new IllegalArgumentException(projectPath.toString());
        }
    }

    public ManagedProject scanProject(String name) {
        IProject project = getProject(name);
        TaskProgressMonitor monitor = null;
        if (project != null) {
            //check if we have the scan project task in queue.
            if (!taskMap.contains(project) || taskMap.get(project).isCanceled()
                    || taskMap.get(project).finished()) {
                monitor = new TaskProgressMonitor();
                taskMap.put(project, monitor);
                executor.submit(new ScanTask(new GraphicDBSimilarityService(project, projectGraphicDatabase), project, monitor));
            } else {
                monitor = taskMap.get(project);
            }
        }
        return new ManagedProject(project, monitor);
    }

    private void restoreProject(Node node) {

    }

    private void searchFileSimilarity(Float threshold, Set<Similarity> similarities, Node fileNode) {
        for (Relationship similarity : fileNode.getRelationships(Direction.BOTH, NodeRelationships.Similar)) {
            Float s = (Float) similarity.getProperty(PropertyNames.SIMILARITY);
            if (s > threshold) {
                similarities.add(new Similarity(
                        new GraphicFileNode(projectGraphicDatabase, fileNode),
                        new GraphicFileNode(projectGraphicDatabase, similarity.getOtherNode(fileNode)),
                        s));
            }
        }
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
