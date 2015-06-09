package edu.sholom.pzks.lab.algorithm;

import edu.sholom.pzks.lab.model.AutoGraphParams;
import edu.sholom.pzks.lab.model.Edge;
import edu.sholom.pzks.lab.model.Vertex;
import edu.sholom.pzks.lab.service.AlgorithmProvider;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Dmytro on 01.06.2015.
 */
public class GraphGenerator {
    public Graph<Vertex, Edge> generate(AutoGraphParams autoGraphParams){
        Graph<Vertex, Edge> graph = new DirectedSparseMultigraph<>();
        List<Vertex> vertices = new ArrayList<>();
        Random random = new Random();
        int verticesWeightSum = 0;

        //generate vertices
        for(int i = 0; i < autoGraphParams.getNumOfVertices(); i++ ){
            int weight = random.nextInt(autoGraphParams.getVertixMaxWeight() - autoGraphParams.getVertixMinWeight()) + autoGraphParams.getVertixMinWeight();
            verticesWeightSum += weight;
            Vertex vertex = new Vertex("T", i, weight);
            vertices.add(vertex);
            graph.addVertex(vertex);
        }

        //calculate edges weight sum
        int edgesWeightSum = (int)((verticesWeightSum / autoGraphParams.getCorrelation()) - verticesWeightSum);
        int maxEdgeWeight = edgesWeightSum / 4;
        maxEdgeWeight = maxEdgeWeight == 0 ? 1 : maxEdgeWeight;
        //generate edges
        int edgesCount = 0;
        add_edge:
        while(edgesWeightSum > 0){
            Vertex from = vertices.get(random.nextInt(autoGraphParams.getNumOfVertices()));
            Vertex to = vertices.get(random.nextInt(autoGraphParams.getNumOfVertices()));
            //generate weight
            int weight = random.nextInt(maxEdgeWeight) + 1;
            weight = weight > edgesWeightSum ? edgesWeightSum : weight;
            //check if edge exists add weight to edge
            //it is done to prevent cases when all possible edges created
            //but edgesWeightSum > 0
            for(Edge e: graph.getOutEdges(from)){
                if(graph.getDest(e).equals(to)) {
                    e.setWeight(e.getWeight() + weight);
                    continue add_edge;
                }

            }
            Edge edge = new Edge("L", edgesCount++, weight);
            graph.addEdge(edge, from, to);
            if (AlgorithmProvider.checkGraphForCycles(graph)) {
                graph.removeEdge(edge);
                continue;
            }
            edgesWeightSum -= weight;
        }
        return graph;
    }
}
