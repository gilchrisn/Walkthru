package com.example.walkthru.items.apihandler;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Element {
    private long id;
    private String type;
    private double lat;
    private double lon;
    private List<Long> nodes;
    private Map<String, String> tags;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public List<Long> getNodes() {
        return nodes;
    }

    public void setNodes(List<Long> nodes) {
        this.nodes = nodes;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Element{" +
                "\n\tid=" + id +
                ",\n\ttype='" + type + '\'' +
                ",\n\tlat=" + lat +
                ",\n\tlon=" + lon +
                ",\n\tnodes=" + nodes +
                ",\n\ttags=" + tags +
                "\n\t}";
    }
}