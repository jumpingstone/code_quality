package com.jumpingstone.codequality.fireeye.neo4j;

import org.neo4j.graphdb.RelationshipType;

/**
 * Created by chenwei on 2018/10/31.
 */
public enum NodeRelationships implements RelationshipType {
    Similar,
    Contains
}
