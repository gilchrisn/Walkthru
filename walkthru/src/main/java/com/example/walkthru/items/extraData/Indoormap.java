package com.example.walkthru.items.extraData;

import java.util.*;

import com.example.walkthru.items.algorithm.Node;
import com.example.walkthru.items.algorithm.Way;

public class Indoormap {
    public static Map<Long, Node> importNodes() {
        Map<Long, Node> returnNodeMap = new HashMap<>();

        // Funan
        returnNodeMap.put(-1L, new Node(-1L, 1.291694, 103.849444, null));
        returnNodeMap.put(-2L, new Node(-2L, 1.291139, 103.850389, null));

        // Raffles City
        returnNodeMap.put(-3L, new Node(-3L, 1.293456, 103.853117, null));
        returnNodeMap.put(-4L, new Node(-4L, 1.294415, 103.853410, null));
        returnNodeMap.put(-5L, new Node(-5L, 1.293562, 103.853858, null));
        returnNodeMap.put(-6L, new Node(-6L, 1.293747, 103.853231, null));
        returnNodeMap.put(-7L, new Node(-7L, 1.294225, 103.853274, null));
        returnNodeMap.put(-8L, new Node(-8L, 1.293666, 103.853507, null));

        // City link
        returnNodeMap.put(-9L, new Node(-9L, 1.292956, 103.852937, null));
        returnNodeMap.put(-10L, new Node(-10L, 1.292510, 103.853697, null));

        // Milenia walk
        returnNodeMap.put(-11L, new Node(-11L, 1.293790, 103.859636, null));
        returnNodeMap.put(-12L, new Node(-12L, 1.291862, 103.859815, null));

        return returnNodeMap;
    }

    public static Set<Way> importWays() {
        Map<Long, Node> nodesMap = importNodes();
        Set<Way> returnWayMap = new HashSet<>();

        // Funan way 1
        List<Node> funan1Nodes = new ArrayList<>();
        funan1Nodes.add(nodesMap.get(-1L));
        funan1Nodes.add(new Node( 7863474387L, 1.291385, 103.849183, null));
        Map<String,String> funan1Tags = new HashMap<>();
        funan1Tags.put("highway", "footway");
        funan1Tags.put("covered", "yes");

        returnWayMap.add(new Way(funan1Nodes, funan1Tags));

        // Funan way 2
        List<Node> funan2Nodes = new ArrayList<>();
        funan2Nodes.add(nodesMap.get(-2L));
        funan2Nodes.add(new Node(7863474399L, 1.290349, 103.849977, null));
        Map<String,String> funan2Tags = new HashMap<>();
        funan2Tags.put("highway", "footway");
        funan2Tags.put("covered", "yes");

        returnWayMap.add(new Way(funan2Nodes, funan2Tags));

        // Funan way 3
        List<Node> funan3Nodes = new ArrayList<>();
        funan3Nodes.add(nodesMap.get(-2L));
        funan3Nodes.add(new Node(4391392122L, 1.291753, 103.850867, null));
        Map<String,String> funan3Tags = new HashMap<>();
        funan3Tags.put("highway", "footway");
        funan3Tags.put("covered", "yes");

        returnWayMap.add(new Way(funan3Nodes, funan3Tags));

        // Funan way 4
        List<Node> funan4Nodes = new ArrayList<>();
        funan4Nodes.add(new Node(7770029612L, 1.291495, 103.849287, null));
        funan4Nodes.add(new Node(7863474387L, 1.291385, 103.849183, null));
        Map<String,String> funan4Tags = new HashMap<>();
        funan4Tags.put("highway", "footway");
        funan4Tags.put("covered", "yes");

        returnWayMap.add(new Way(funan4Nodes, funan4Tags));

        // Funan way 5
        List<Node> funan5Nodes = new ArrayList<>();
        funan5Nodes.add(nodesMap.get(-1L));
        funan5Nodes.add(nodesMap.get(-2L));
        Map<String,String> funan5Tags = new HashMap<>();
        funan5Tags.put("highway", "footway");
        funan5Tags.put("indoor", "yes");

        returnWayMap.add(new Way(funan5Nodes, funan5Tags));

        // Raffles City way 1
        List<Node> rafflesCity1Nodes = new ArrayList<>();
        rafflesCity1Nodes.add(nodesMap.get(-3L));
        rafflesCity1Nodes.add(nodesMap.get(-6L));
        Map<String,String> rafflesCity1Tags = new HashMap<>();
        rafflesCity1Tags.put("highway", "footway");
        rafflesCity1Tags.put("indoor", "yes");

        returnWayMap.add(new Way(rafflesCity1Nodes, rafflesCity1Tags));

        // Raffles City way 2
        List<Node> rafflesCity2Nodes = new ArrayList<>();
        rafflesCity2Nodes.add(nodesMap.get(-7L));
        rafflesCity2Nodes.add(nodesMap.get(-6L));
        Map<String,String> rafflesCity2Tags = new HashMap<>();
        rafflesCity2Tags.put("highway", "footway");
        rafflesCity2Tags.put("indoor", "yes");

        returnWayMap.add(new Way(rafflesCity2Nodes, rafflesCity2Tags));

        // Raffles City way 3
        List<Node> rafflesCity3Nodes = new ArrayList<>();
        rafflesCity3Nodes.add(nodesMap.get(-7L));
        rafflesCity3Nodes.add(nodesMap.get(-4L));
        Map<String,String> rafflesCity3Tags = new HashMap<>();
        rafflesCity3Tags.put("highway", "footway");
        rafflesCity3Tags.put("indoor", "yes");

        returnWayMap.add(new Way(rafflesCity3Nodes, rafflesCity3Tags));

        // Raffles City way 4
        List<Node> rafflesCity4Nodes = new ArrayList<>();
        rafflesCity4Nodes.add(nodesMap.get(-7L));
        rafflesCity4Nodes.add(nodesMap.get(-8L));
        Map<String,String> rafflesCity4Tags = new HashMap<>();
        rafflesCity4Tags.put("highway", "footway");
        rafflesCity4Tags.put("indoor", "yes");

        returnWayMap.add(new Way(rafflesCity4Nodes, rafflesCity4Tags));

        // Raffles City way 5
        List<Node> rafflesCity5Nodes = new ArrayList<>();
        rafflesCity5Nodes.add(nodesMap.get(-5L));
        rafflesCity5Nodes.add(nodesMap.get(-8L));
        Map<String,String> rafflesCity5Tags = new HashMap<>();
        rafflesCity5Tags.put("highway", "footway");
        rafflesCity5Tags.put("indoor", "yes");

        returnWayMap.add(new Way(rafflesCity5Nodes, rafflesCity5Tags));

        // Raffles City way 6
        List<Node> rafflesCity6Nodes = new ArrayList<>();
        rafflesCity6Nodes.add(nodesMap.get(-6L));
        rafflesCity6Nodes.add(nodesMap.get(-8L));
        Map<String,String> rafflesCity6Tags = new HashMap<>();
        rafflesCity6Tags.put("highway", "footway");
        rafflesCity6Tags.put("indoor", "yes");

        returnWayMap.add(new Way(rafflesCity6Nodes, rafflesCity6Tags));

        // Raffles City way 7
        List<Node> rafflesCity7Nodes = new ArrayList<>();
        rafflesCity7Nodes.add(nodesMap.get(-3L));
        rafflesCity7Nodes.add(new Node(1839614199L, 1.293299, 103.853039, null));
        Map<String,String> rafflesCity7Tags = new HashMap<>();
        rafflesCity7Tags.put("highway", "footway");
        rafflesCity7Tags.put("covered", "yes");

        returnWayMap.add(new Way(rafflesCity7Nodes, rafflesCity7Tags));

        // Raffles City way 8
        List<Node> rafflesCity8Nodes = new ArrayList<>();
        rafflesCity8Nodes.add(new Node(7723346591L, 1.294757, 103.853451, null));
        rafflesCity8Nodes.add(nodesMap.get(-4L));
        Map<String,String> rafflesCity8Tags = new HashMap<>();
        rafflesCity8Tags.put("highway", "footway");
        rafflesCity8Tags.put("covered", "yes");

        returnWayMap.add(new Way(rafflesCity8Nodes, rafflesCity8Tags));

        // Raffles City way 9
        List<Node> rafflesCity9Nodes = new ArrayList<>();
        rafflesCity9Nodes.add(nodesMap.get(-5L));
        rafflesCity9Nodes.add(new Node(1.294027, 103.854204));
        rafflesCity9Nodes.add(new Node(3961943200L, 1.293848, 103.854485, null));
        Map<String,String> rafflesCity9Tags = new HashMap<>();
        rafflesCity9Tags.put("highway", "footway");
        rafflesCity9Tags.put("covered", "yes");

        returnWayMap.add(new Way(rafflesCity9Nodes, rafflesCity9Tags));

        // City Link way 1
        List<Node> cityLink1Nodes = new ArrayList<>();
        cityLink1Nodes.add(nodesMap.get(-9L));
        cityLink1Nodes.add(nodesMap.get(-10L));
        cityLink1Nodes.add(new Node(4560997714L, 1.292677, 103.854025, null));
        Map<String, String> cityLink1Tags = new HashMap<>();
        cityLink1Tags.put("highway", "footway");
        cityLink1Tags.put("indoor", "yes");
        cityLink1Tags.put("tunnel", "yes");
        cityLink1Tags.put("layer", "-1");

        returnWayMap.add(new Way(cityLink1Nodes, cityLink1Tags));

        // City Link way 2
        List<Node> cityLink2Nodes = new ArrayList<>();
        cityLink2Nodes.add(new Node(9354942756L, 1.293083, 103.852323, null));
        cityLink2Nodes.add(nodesMap.get(-9L));
        Map<String, String> cityLink2Tags = new HashMap<>();
        cityLink2Tags.put("highway", "footway");
        cityLink2Tags.put("indoor", "yes");
        cityLink2Tags.put("tunnel", "yes");
        cityLink2Tags.put("layer", "-1");

        returnWayMap.add(new Way(cityLink2Nodes, cityLink2Tags));

        // City Link way 3
        List<Node> cityLink3Nodes = new ArrayList<>();
        cityLink3Nodes.add(new Node(9354942756L, 1.293083, 103.852323, null));
        cityLink3Nodes.add(nodesMap.get(-3L));
        Map<String, String> cityLink3Tags = new HashMap<>();
        cityLink3Tags.put("highway", "footway");
        cityLink3Tags.put("indoor", "yes");
        cityLink3Tags.put("tunnel", "yes");
        cityLink3Tags.put("layer", "-1");

        returnWayMap.add(new Way(cityLink3Nodes, cityLink3Tags));

        // Milenia way 1
        List<Node> milenia1Nodes = new ArrayList<>();
        milenia1Nodes.add(nodesMap.get(-11L));
        milenia1Nodes.add(nodesMap.get(-12L));
        Map<String, String> milenia1Tags = new HashMap<>();
        milenia1Tags.put("highway", "footway");
        milenia1Tags.put("indoor", "yes");

        returnWayMap.add(new Way(milenia1Nodes, milenia1Tags));

        return returnWayMap;
    }
}