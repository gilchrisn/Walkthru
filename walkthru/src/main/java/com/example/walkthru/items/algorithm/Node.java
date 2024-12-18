package com.example.walkthru.items.algorithm;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;

public class Node {
    @JsonIgnore
    private long id;
    @JsonProperty("nodeName")
    private String name;
    private double lat;
    private double lon;

    @JsonIgnore
    private Map<String, String> tags;
    @JsonIgnore
    private Node parent;
    @JsonIgnore
    private double cost;

    public Node(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }
    public Node(double lat, double lon, Map<String, String> tags) {
        this.lat = lat;
        this.lon = lon;
        this.tags = tags;
    }

    public Node(long id, double lat, double lon, Map<String, String> tags) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.tags = tags;
    }

    public long getId(){
        return this.id;
    }
    public void setId(long id){
        this.id = id;
    }
    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Node{" +
                "nodeId:" + id +
                ", nodeName:" + "none" +
                ", lat:" + lat +
                ", lon:" + lon +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Double.compare(lat, node.lat) == 0 && Double.compare(lon, node.lon) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lon, tags);
    }

    @JsonGetter("nodeId")
    public String nodeId(){
        return Long.toString(id);
    }
}
