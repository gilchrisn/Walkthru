package com.example.walkthru.items.frontendformat;

import com.example.walkthru.items.algorithm.Node;
import com.example.walkthru.items.algorithm.Way;

import java.util.*;


public class Graph {
    private List<Node> nodes;
    private List<Way> edges;
    private String startNodeId;
    private String endNodeId;
    // Additional attributes


    public Graph(List<Node> nodes, List<Way> edges, String startNodeId, String endNodeId) {
        this.nodes = nodes;
        this.edges = edges;
        this.startNodeId = startNodeId;
        this.endNodeId = endNodeId;
    }

    // Getters and Setters
    public List<Node> getNodes() { return nodes; }
    public void setNodes(List<Node> nodes) { this.nodes = nodes; }

    public List<Way> getEdges() { return edges; }
    public void setEdges(List<Way> edges) { this.edges = edges; }

    public String getStartNodeId() { return startNodeId; }
    public void setStartNodeId(String startNodeId) { this.startNodeId = startNodeId; }

    public String getEndNodeId() { return endNodeId; }
    public void setEndNodeId(String endNodeId) { this.endNodeId = endNodeId; }

}