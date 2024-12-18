package com.example.walkthru.items.algorithm;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.lang.Double.parseDouble;

@JsonRootName(value = "Edge")
public class Way {

    private long Id;
    @JsonIgnore
    private List<Node> nodes;
    @JsonIgnore
    private Map<String, String> tags;
    @JsonIgnore
    private String altitude = "";

    public Way(List<Node> nodes, Map<String, String> tags) {
        this.nodes = nodes;
        this.tags = tags;
    }

    public String getAltitude(){
        return altitude;
    }

    public void generateAltitude(){
        if (tags == null){
            return;
        }
        List<String> tagList = new ArrayList<>(tags.keySet());

        if (tagList.contains("bridge") && (tags.get("bridge").equals("yes") || tags.get("bridge").equals("covered"))){
            this.altitude = "above ground";
            return;
        }
//        else if (tagList.contains("level") && parseDouble(tags.get("level")) > 0){
//            this.altitude = "above ground";
//            return;
//        }
//        else if (tagList.contains("level") && parseDouble(tags.get("level")) < 0){
//            this.altitude = "under ground";
//            return;
//        }
        else if (tagList.contains("underground") && tags.get("underground").equals("yes")){
            this.altitude = "under ground";
            return;
        }
        else if (tagList.contains("tunnel") && !tags.get("tunnel").equals("no")){
            this.altitude = "under ground";
            return;
        }

        this.altitude = "ground";
    }
    public void setAltitude(String altitude){
        this.altitude = altitude;
    }
    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return "Way{" +
                "fromNodeId:" + nodes.get(0) +
                ", toNodeId:" + nodes.get(nodes.size() - 1) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Way way = (Way) o;
        return Objects.equals(nodes, way.nodes) && Objects.equals(tags, way.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodes, tags);
    }

    @JsonGetter("fromNodeId")
    public String getFromNodeId(){
        return Long.toString(nodes.get(0).getId());
    }

    @JsonGetter("toNodeId")
    public String getToNodeId(){
        return Long.toString(nodes.get(nodes.size() - 1).getId());
    }
}
