package edu.sholom.pzks.lab.algorithm.scheduler.utils;

import edu.sholom.pzks.lab.model.Edge;
import edu.sholom.pzks.lab.model.Vertex;
import edu.uci.ics.jung.graph.Graph;

import java.util.*;

/**
 * Created by Dmytro on 01.06.2015.
 */
public class CriticalPathCounter {

    private Graph<Vertex, Edge> graph;
    HashMap<Vertex, CountedVertex> vertexMap;
    List<CountedVertex> result;

    public CriticalPathCounter(Graph<Vertex, Edge> graph){
        this.graph = graph;
        vertexMap = new HashMap<>();
        result = new LinkedList<>();
        graph.getVertices().forEach((vertex) -> {
            CountedVertex countedVertex = new CountedVertex(vertex);
            vertexMap.put(vertex, countedVertex);
            result.add(countedVertex);
        });
    }

    public CriticalPathCounter countForwardPathLengthsByWeight(){
        Queue<CountedVertex> queue = new LinkedList<>();

        graph.getVertices().forEach((vertex) -> {
            if(graph.getPredecessors(vertex).size() == 0){
                queue.add(vertexMap.get(vertex));
            }
        });

        CountedVertex currentCountedVertex;
        while ((currentCountedVertex = queue.poll()) != null) {
            final int predecessorPathLength = currentCountedVertex.getMaxWToStart();
            final Vertex currVertex = currentCountedVertex.getVertex();
            graph.getSuccessors(currVertex).forEach((vertex) -> {
                CountedVertex childVertex = vertexMap.get(vertex);
                int maxWToStart = predecessorPathLength + currVertex.getWeight();
                if (childVertex.getMaxWToStart() < maxWToStart) {
                    childVertex.setMaxWToStart(maxWToStart);
                }
                queue.add(childVertex);
            });
        }

        return this;
    }

    public CriticalPathCounter countBackwardPathLengthsbyWeigth(){

        Queue<CountedVertex> queue = new LinkedList<>();

        graph.getVertices().forEach((vertex) -> {
            if(graph.getSuccessors(vertex).size() == 0){
                CountedVertex endVertex = vertexMap.get(vertex);
                endVertex.setMaxWToEnd(vertex.getWeight());
                queue.add(endVertex);

            }
        });

        CountedVertex currentCountedVertex;
        while ((currentCountedVertex = queue.poll()) != null) {
            final int successorPathLength = currentCountedVertex.getMaxWToEnd();
            graph.getPredecessors(currentCountedVertex.getVertex()).forEach((vertex) -> {
                CountedVertex parentVertex = vertexMap.get(vertex);
                int maxWToEnd = successorPathLength + vertex.getWeight();
                if (parentVertex.getMaxWToEnd() < maxWToEnd) {
                    parentVertex.setMaxWToEnd(maxWToEnd);
                }
                queue.add(parentVertex);
            });
        }

        return this;
    }

    public CriticalPathCounter countForwardPathLengthsByAmount(){
        Queue<CountedVertex> queue = new LinkedList<>();

        graph.getVertices().forEach((vertex) -> {
            if(graph.getPredecessors(vertex).size() == 0){
                queue.add(vertexMap.get(vertex));
            }
        });

        CountedVertex currentCountedVertex;
        while ((currentCountedVertex = queue.poll()) != null) {
            final int predecessorAmount= currentCountedVertex.getMaxNToStart();
            graph.getSuccessors(currentCountedVertex.getVertex()).forEach((vertex) -> {
                CountedVertex childVertex = vertexMap.get(vertex);
                int maxNToStart = predecessorAmount + 1;
                if (childVertex.getMaxNToStart() < maxNToStart) {
                    childVertex.setMaxNToStart(maxNToStart);
                }
                queue.add(childVertex);
            });
        }

        return this;
    }

    public CriticalPathCounter countBackwardPathLengthsByAmount(){

        Queue<CountedVertex> queue = new LinkedList<>();

        graph.getVertices().forEach((vertex) -> {
            if(graph.getSuccessors(vertex).size() == 0){
                CountedVertex endVertex = vertexMap.get(vertex);
                endVertex.setMaxNToEnd(1);
                queue.add(endVertex);

            }
        });

        CountedVertex currentCountedVertex;
        while ((currentCountedVertex = queue.poll()) != null) {
            final int successorAmount = currentCountedVertex.getMaxNToEnd();
            graph.getPredecessors(currentCountedVertex.getVertex()).forEach((vertex) -> {
                CountedVertex parentVertex = vertexMap.get(vertex);
                int maxNToEnd = successorAmount + 1;
                if (parentVertex.getMaxNToEnd() < maxNToEnd) {
                    parentVertex.setMaxNToEnd(maxNToEnd);
                }
                queue.add(parentVertex);
            });
        }

        return this;
    }


    public List<CountedVertex> getcountedVertices(){

        result.forEach(System.out::println);
        return result;
    }

}
