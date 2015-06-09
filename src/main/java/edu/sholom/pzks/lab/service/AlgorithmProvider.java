package edu.sholom.pzks.lab.service;

import edu.sholom.pzks.lab.algorithm.CycleDetector;
import edu.sholom.pzks.lab.model.Edge;
import edu.sholom.pzks.lab.model.Vertex;
import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import org.apache.commons.collections15.Transformer;

import java.util.Set;

/**
 * Created by Dmytro on 22.05.2015.
 */
public class AlgorithmProvider {
    public static boolean checkGraphConnectivity(Graph<Vertex, Edge> graph){
        Transformer<Graph<Vertex,Edge>, Set<Set<Vertex>>> trns = new WeakComponentClusterer<>();
        Set<Set<Vertex>> clusters = trns.transform(graph);
        System.out.println(clusters.size());

        return clusters.size() == 1 ? true : false;
    }

    public static boolean checkGraphForCycles(Graph<Vertex, Edge> graph){
        CycleDetector cycleDetector = new CycleDetector((DirectedSparseMultigraph<Vertex, Edge>)graph);
        return cycleDetector.isAcyclic();
    }


}
