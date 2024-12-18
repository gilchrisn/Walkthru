package com.example.walkthru.service;

import java.util.*;

import com.example.walkthru.items.algorithm.Node;
import com.example.walkthru.items.algorithm.Way;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    public static final double EARTH_RADIUS_M = 6371;
    public static double distance(Node n1, Node n2){
        double lat1 = n1.getLat();
        double lat2 = n2.getLat();
        double lon1 = n1.getLon();
        double lon2 = n2.getLon();

        // Convert degrees to radians
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double rLat1 = Math.toRadians(lat1);
        double rLat2 = Math.toRadians(lat2);

        // Compute 'a'
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(rLat1) * Math.cos(rLat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        // Compute 'c'
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Compute distance
        return EARTH_RADIUS_M * c;
    }

    public static void newEdge(Node n1, Node n2, Map<String, String> details, Map<Node, Map<Node, Map<String, String>>> graph){
        if (!graph.containsKey(n1)){
            graph.put(n1, new HashMap<Node, Map<String, String>>());
        }

        if (!graph.get(n1).containsKey(n2)){
            graph.get(n1).put(n2, new HashMap<String, String>());
        }

        graph.get(n1).get(n2).putAll(details);
    }

    public static void connectGraph(Set<Node> nodes, Map<Node, Map<Node, Map<String, String>>> graph){
        List<Node> nodeList = new ArrayList<>(nodes);
        Iterator<Node> iter1 = nodeList.listIterator();

        int index = 0;
        while(iter1.hasNext()){
            Node node1 = iter1.next();
            Iterator<Node> iter2 = nodeList.listIterator(index + 1);

            while(iter2.hasNext()){
                Node node2 = iter2.next();

                if (distance(node1, node2) < 0.011){
                    newEdge(node1, node2, new HashMap<>(), graph);
                    newEdge(node1, node2, new HashMap<>(), graph);
                }
            }
            index++;
        }
    }

    public static Map<Node, Map<Node, Map<String, String>>> constructGraph(Map<Long, Node> nodes, Set<Way> ways){
        Map<Node, Map<Node, Map<String, String>>> graph = new HashMap<>();

        //initialize all node
        Collection<Node> initialize = nodes.values();
        Iterator<Node> initIter = initialize.iterator();

        while(initIter.hasNext()){
            graph.put(initIter.next(), new HashMap<Node, Map<String, String>>());
        }

        Iterator<Way> iter = ways.iterator();
        while (iter.hasNext()){
            Way tempWay = iter.next();

            List<Node> tempNodeId = tempWay.getNodes();
            int size = tempNodeId.size();
            for (int i = 0; i < size - 1; i++){

                Node n1 = nodes.get(tempNodeId.get(i).getId());
                Node n2 = nodes.get(tempNodeId.get(i + 1).getId());

                if (n1 == null || n2 == null){
                    continue;
                }

                newEdge(n1, n2, tempWay.getTags(), graph);
                newEdge(n2, n1, tempWay.getTags(), graph);

            }
        }

        return graph;
    }

    public static String shelterAvailability(Way way){
        Map<String, String> tags = way.getTags();

        Set<String> keyset = tags.keySet();
        Iterator<String> iter = keyset.iterator();

        while(iter.hasNext()){
            String comfortLevel = "outdoor";

            String key = iter.next();
            String value = tags.get(key);

            if (key.equals("highway") && value.equals("corridor")){
                comfortLevel = "AC";
            }
            else if (key.equals("level")){
                comfortLevel = "AC";
            }
            else if (key.equals("underground") && value.equals("yes")){
                comfortLevel = "AC";
            }
            else if (key.equals("covered")){
                if (value.equals("corridor")) {
                    comfortLevel = "AC";
                }
                else if (!value.equals("no")) {
                    comfortLevel = "sheltered";
                }

            }
            else if (key.equals("indoor")) {
                comfortLevel = "AC";
            }
            else if (key.equals("bridge") && value.equals("covered")){
                comfortLevel = "AC";
            }
            else if (key.equals("layer")){
                comfortLevel = "AC";
            }
            else if (key.equals("tunnel") && !value.equals("no")){
                comfortLevel = "AC";
            }

            if (comfortLevel.equals("AC")){
                return "AC";
            }
        }


        return "outdoor";
    }

    public static double pathLength(Way way){
        List<Node> nodes = way.getNodes();

        double totalDistance = 0;
        long size = nodes.size();
        for(int i = 0; i < size - 1; i++){
            totalDistance += distance(nodes.get(i), nodes.get(i + 1));
        }
        return totalDistance;
    }
}
