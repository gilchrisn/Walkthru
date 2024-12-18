package com.example.walkthru.service;

import com.example.walkthru.items.algorithm.Node;
import com.example.walkthru.items.algorithm.PriorityQueue;
import com.example.walkthru.items.algorithm.Way;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.walkthru.service.PathService.distance;

@Service
public class PathfindingService {
    private Map<Node, Map<Node, Map<String, String>>> graph;

    public PathfindingService(Map<Node, Map<Node, Map<String, String>>> graph){
        this.graph = graph;
    }

    public double calculateWeight(Node n1, Node n2, String temperature, String rainfall, String shelter){
        double distance = distance(n1, n2) * 10;
        double temperatureMultiplier = 1;
        double rainfallMultiplier = 1;
        double shelterMultiplier = 1;

        if (shelter.equals("AC")){
            shelterMultiplier = 1;
        }
        else if (shelter.equals("sheltered")){
            shelterMultiplier = 1.2;
        }
        else{
            if (temperature.equals("very-hot")){
                temperatureMultiplier = 4;
            }
            else if (temperature.equals("hot")){
                temperatureMultiplier = 3;
            }

            if (rainfall.equals("rain")){
                rainfallMultiplier = 5;
            }
        }


        return distance * temperatureMultiplier * rainfallMultiplier * shelterMultiplier;
    }
    public Node findClosestNode(Node n, Set<Node> explored){
        Set<Node> nodes = graph.keySet();
        Iterator<Node> iter = nodes.iterator();

        double min = Double.MAX_VALUE;
        Node target = null;
        while (iter.hasNext()){
            Node current = iter.next();

            if (explored.contains(current)){
                continue;
            }

            double dist = distance(current, n);

            if (dist < min){
                min = dist;
                target = current;
            }
        }

        return target;
    }

    private Set<Node> action(Node n){
        Map<Node, Map<String, String>> nodes = graph.get(n);
        if (nodes == null){
            return new HashSet<>();
        }
        return nodes.keySet();
    }

    public List<Node> findRoute(double lat1, double lon1, double lat2, double lon2, String temperature, String rainfall){
        //explored nodes
        Set<Node> explored = new HashSet<>();

        //create starting node
        Node start = new Node(lat1, lon1);

        //connect starting node to closest node
        Node closestStartNode = findClosestNode(start, explored);
        PathService.newEdge(closestStartNode, start,new HashMap<>(), graph);
        PathService.newEdge(start, closestStartNode,new HashMap<>(), graph);

        //create end node and connect to closest node
        Node end = new Node(lat2, lon2);

        Node closestEndNode = findClosestNode(end, explored);
        PathService.newEdge(closestEndNode, end,new HashMap<>(), graph);
        PathService.newEdge(end, closestEndNode,new HashMap<>(), graph);

        //create frontier
        PriorityQueue frontier = new PriorityQueue(end);

        // Add start to frontier.
        frontier.add(start);

        System.out.println("min path = " + distance(start, end));
        //A* algorithm
        Node currentNode = null;
        long id = 1;



        while (true){
            //in case of dead end and no other edge options
            if (frontier.isEmpty() && !currentNode.equals(closestEndNode)){
                Node desperateNode = findClosestNode(currentNode, explored);

                desperateNode.setParent(currentNode);
                desperateNode.setCost(currentNode.getCost() + calculateWeight(currentNode, desperateNode, temperature, rainfall, "None"));
                desperateNode.setId(id);

                frontier.add(desperateNode);
            }

            //take next node from frontier
            currentNode = frontier.remove();

            if (explored.contains(currentNode)){
                continue;
            }

            if (currentNode.equals(closestEndNode)){
                break;
            }
            //update explored node
            explored.add(currentNode);

            Set<Node> nextNode = action(currentNode);

            for (Node node : nextNode){
                Node newNode = new Node(node.getLat(), node.getLon());

                if (explored.contains(newNode)){
                    continue;
                }

                if (node.getLon() < 103.620535 || node.getLon() > 104.031005 ||node.getLat() < 1.238717 || node.getLat() > 1.470193){
                    continue;
                }

                //check availability of shelter
                String shelter = PathService.shelterAvailability(new Way(Arrays.asList(currentNode, newNode), graph.get(currentNode).get(newNode)));

                //configure new node
                newNode.setCost(currentNode.getCost() + calculateWeight(currentNode, newNode, temperature, rainfall, shelter));
                newNode.setParent(currentNode);
                newNode.setId(id);

                frontier.add(newNode);
            }
            id++;
        }
        currentNode.setId(id);

        //create route
        List<Node> route = new ArrayList<>();
        while (currentNode.getParent() != null){
            route.add(currentNode);
            currentNode = currentNode.getParent();
        }

        route.add(0, end);

        return route;
    }
}
