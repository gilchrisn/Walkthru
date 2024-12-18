package com.example.walkthru.items.algorithm;

import com.example.walkthru.items.algorithm.Node;

import java.util.*;

import static com.example.walkthru.service.PathService.distance;

public class PriorityQueue {
    private List<Node> nodes = new ArrayList<>();
    private Node target;

    public PriorityQueue(Node target){
        this.target = target;
    }

    public void add(Node n){
        nodes.add(n);
    }

    public Node remove(){
        if (nodes.isEmpty()){
            return null;
        }

//        Collections.sort(nodes, Comparator.comparingDouble(n -> n.getCost() + distance(n, target)));
        Collections.sort(nodes, Comparator.comparingDouble(n -> n.getCost()));

        Node node = nodes.get(0);
        nodes.remove(0);

        return node;
    }

    public boolean isEmpty(){
        return nodes.isEmpty();
    }

    public boolean contains(Node n){
        return nodes.contains(n);
    }
}
