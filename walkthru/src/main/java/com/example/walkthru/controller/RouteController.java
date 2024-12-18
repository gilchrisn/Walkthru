package com.example.walkthru.controller;

import com.example.walkthru.items.extraData.Indoormap;
import com.example.walkthru.items.frontendformat.Graph;
import com.example.walkthru.service.*;
import com.example.walkthru.items.apihandler.Element;
import com.example.walkthru.items.algorithm.Node;
import com.example.walkthru.items.algorithm.Way;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class RouteController {

    private final PathService pathService;
    private final OpenStreetMapService openStreetMapService;
    private final MeteoblueService meteoblueService;
    private final RainfallService rainfallService;
    private Map<Long, Node> nodes;
    private Set<Way> ways;
    private Map<Node, Map<Node, Map<String, String>>> graph;

    public RouteController(OpenStreetMapService openStreetMapService, PathService pathService, MeteoblueService meteoblueService, RainfallService rainfallService){
        this.openStreetMapService = openStreetMapService;
        this.pathService = pathService;
        this.meteoblueService = meteoblueService;
        this.rainfallService = rainfallService;

        // fetch data from OSM
        System.out.println("fetching data from OSM...");;
        List<Element> elements = openStreetMapService.fetchData();

        //separate raw data into temporary database
        System.out.println("separating nodes and ways...");
        Map<Long, Node> nodes = new HashMap<>();
        Set<Way> ways = new HashSet<>();
        openStreetMapService.separateToNodesAndWays(elements, nodes, ways);
        nodes.putAll(Indoormap.importNodes());
        ways.addAll(Indoormap.importWays());

        this.nodes = nodes;
        this.ways = ways;

        // construct graph
        System.out.println("constructing graph...");
        Map<Node, Map<Node, Map<String, String>>> graph = PathService.constructGraph(nodes, ways);
        PathService.connectGraph(new HashSet<Node>(nodes.values()), graph);
        this.graph = graph;
        System.out.println("there are " + graph.keySet().size() + " nodes");
    }

    @GetMapping("/")
    public String health(){
        return "Hello, World!";
    }

//    @PostMapping("/nodes")
//    public Graph createGraphData(@RequestBody double startlat, @RequestBody double startlon, @RequestBody double endlat, @RequestBody double endlon){
//        return openStreetMapData(startlat, startlon, endlat, endlon);
//    }



    //@RequestParam double startlat, @RequestParam double startlon, @RequestParam double endlat, @RequestParam double endlon
    @GetMapping("/nodes")
    public Graph openStreetMapData(@RequestParam double startlat, @RequestParam double startlon, @RequestParam double endlat, @RequestParam double endlon){


//        double startlat = 1.3044889;
//        double startlon = 103.8461121;
//        double endlat = 1.3036678;
//        double endlon = 103.8550412 ;

        if (startlat == endlat && startlon == endlon){
            return new Graph(new ArrayList<Node>(), new ArrayList<Way>(), "0", "0");
        }

        //fetch weather data

        String temperature = meteoblueService.getCurrentTemperature(startlat, startlon);
        System.out.println("meteoblue temperature = " + temperature);
//        String temperature = "hello";
        String rainfall = rainfallService.getCurrentRainfall("S119");

        //find path
        System.out.println("finding path");
        PathfindingService pathfindingService = new PathfindingService(graph);
        List<Node> route = pathfindingService.findRoute(startlat, startlon, endlat, endlon, temperature, rainfall);
        Collections.reverse(route);


        System.out.println("length = " + 1000 * PathService.pathLength(new Way(route, null)));
        return new Graph(route, routeWays(route), Long.toString(route.get(0).getId()), Long.toString(route.get(route.size()-1).getId()));


    }



    public List<Way> routeWays(List<Node> nodes){
        List<Way> ways = new ArrayList<>();

        int size = nodes.size();
        for (int i = 0; i < size - 1; i++){
            Node n1 = nodes.get(i);
            Node n2 = nodes.get(i+1);
            Way edge = new Way(new ArrayList<Node>(Arrays.asList(n1, n2)), graph.get(n1).get(n2));
            edge.setId(i + 1);
            edge.generateAltitude();
            ways.add(edge);
        }
        return ways;
    }
}
