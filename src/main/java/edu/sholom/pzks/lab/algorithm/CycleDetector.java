package edu.sholom.pzks.lab.algorithm;

import edu.sholom.pzks.lab.model.Edge;
import edu.sholom.pzks.lab.model.Vertex;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dmytro on 22.05.2015.
 */
public class CycleDetector {
    private DirectedSparseMultigraph<Vertex, Edge> graph;
    private boolean cyclic;
    private HashMap<Vertex, ColoredVertex> vertexMap;
    public CycleDetector(DirectedSparseMultigraph<Vertex, Edge> graph){
        this.graph = graph;
        this.cyclic = false;
        vertexMap = new HashMap<>();
    }

    public boolean isAcyclic(){
        List<ColoredVertex> coloredVertices = new ArrayList<>();
        for(Vertex vertex: graph.getVertices()){
            ColoredVertex coloredVertex = new ColoredVertex(vertex);
            coloredVertices.add(coloredVertex);
            vertexMap.put(vertex, coloredVertex);
        }
        for(ColoredVertex coloredVertex: coloredVertices){
            dfs_cycle(coloredVertex);
        }
        return cyclic;
    }

    private void dfs_cycle(ColoredVertex coloredVertex){
        if(coloredVertex.color == Color.BLACK) return;
        if(cyclic) return;
        if(coloredVertex.color == Color.GRAY){
            cyclic = true;
            return;
        }
        coloredVertex.color = Color.GRAY;
        for(Vertex vertex: graph.getSuccessors(coloredVertex.vertex)){
            dfs_cycle(vertexMap.get(vertex));
            if(cyclic) return;
        }
        coloredVertex.color = Color.BLACK;
    }

    private static class ColoredVertex{
        final public Vertex vertex;
        public Color color;
        public ColoredVertex(Vertex vertex){
            this.vertex = vertex;
            this.color = Color.WHITE;
        }
    }
}
