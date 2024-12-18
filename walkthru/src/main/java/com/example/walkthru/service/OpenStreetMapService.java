package com.example.walkthru.service;


import com.example.walkthru.items.apihandler.Element;
import com.example.walkthru.items.algorithm.Node;
import com.example.walkthru.items.apihandler.OverpassResponse;
import com.example.walkthru.items.algorithm.Way;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class OpenStreetMapService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public OpenStreetMapService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<Element> fetchData() {
        String url = "http://overpass-api.de/api/interpreter?data=" + "[out:json][timeout:100];(way[\"highway\"=\"pedestrian\"][\"access\"!~\"no|permissive|customer|private\"](1.2884,103.8465,1.3048,103.8631);way[\"highway\"=\"steps\"][\"access\"!~\"no|permissive|customer|private\"](1.2884,103.8465,1.3048,103.8631);way[\"highway\"=\"living_street\"][\"access\"!~\"no|permissive|customer|private\"](1.2884,103.8465,1.3048,103.8631);way[\"highway\"=\"footway\"][\"access\"!~\"no|permissive|customer|private\"](1.2884,103.8465,1.3048,103.8631);way[\"highway\"=\"crossing\"][\"access\"!~\"no|permissive|customer|private\"](1.2884,103.8465,1.3048,103.8631);way[\"highway\"=\"corridor\"][\"access\"!~\"no|permissive|customer|private\"](1.2884,103.8465,1.3048,103.8631);way[\"footway\"=\"sidewalk\"][\"access\"!~\"no|permissive|customer|private\"](1.2884,103.8465,1.3048,103.8631);way[\"footway\"=\"crossing\"][\"access\"!~\"no|permissive|customer|private\"](1.2884,103.8465,1.3048,103.8631);way[\"footway\"=\"access_aisle\"][\"access\"!~\"no|permissive|customer|private\"](1.2884,103.8465,1.3048,103.8631);way[\"footway\"=\"link\"][\"access\"!~\"no|permissive|customer|private\"](1.2884,103.8465,1.3048,103.8631);way[\"escalator\"=\"yes\"][\"access\"!~\"no|permissive|customer|private\"](1.2884,103.8465,1.3048,103.8631););out body;>;out skel qt;";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String jsonResponse = response.getBody();

        try {
            OverpassResponse overpassResponse = objectMapper.readValue(jsonResponse, OverpassResponse.class);
            return overpassResponse.getElements();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void separateToNodesAndWays(List<Element> elements, Map<Long, Node> nodes, Set<Way> ways){

        Iterator<Element> iter = elements.iterator();

        while(iter.hasNext()){
            Element e = iter.next();

            if (e.getType().equals("node")){
                nodes.put(e.getId(), new Node(e.getId(), e.getLat(), e.getLon(), e.getTags()));

            }
        }


        iter = elements.iterator();
        while(iter.hasNext()){
            Element e = iter.next();

            if (e.getType().equals("way")){
                List<Long> nodeIds = e.getNodes();
                List<Node> wayNodes = new ArrayList<>();
                for(Long l : nodeIds) {
                    wayNodes.add(nodes.get(l));
                }
                ways.add(new Way(wayNodes, e.getTags()));
            }
        }
//        for(Way w : ways){
//            System.out.println(w.getNodes());
//        }
    }

    public boolean tagInNode(Set<Node> nodes){
        Iterator<Node> iter = nodes.iterator();

        while(iter.hasNext()){
            if (iter.next().getTags() != null){
                return true;
            }
        }

        return false;
    }

    public Map<String, Set<String>> allPossibleTags(Set<Way> ways){
        Iterator<Way> iter = ways.iterator();

        Map<String, Set<String>> tagList = new HashMap<>();

        while(iter.hasNext()){
            Map<String,String> tags = iter.next().getTags();

            Set<String> keySet = tags.keySet();
            Iterator<String> individualIter = keySet.iterator();

            while(individualIter.hasNext()) {
                String key = individualIter.next();

                if (!tagList.containsKey(key)) {
                    tagList.put(key, new HashSet<String>());
                }

                tagList.get(key).add(tags.get(key));
            }
        }

        return tagList;
    }

}
