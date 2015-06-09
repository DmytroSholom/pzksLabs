package edu.sholom.pzks.lab.service;

import edu.sholom.pzks.lab.model.Edge;
import edu.sholom.pzks.lab.model.Vertex;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.io.GraphIOException;
import edu.uci.ics.jung.io.GraphMLWriter;
import edu.uci.ics.jung.io.graphml.*;
import org.apache.commons.collections15.Transformer;

import java.io.*;

/**
 * Created by Dmytro on 22.05.2015.
 */
public class GraphIO {

    public static void writeToFile(File file, Graph<Vertex, Edge> graph, AbstractLayout<Vertex, Edge> layout) throws IOException {
        GraphMLWriter<Vertex, Edge> graphWriter = new GraphMLWriter<> ();

        PrintWriter out = new PrintWriter( new BufferedWriter(new FileWriter(file)));

        graphWriter.addVertexData("x", null, "0",(v) -> Double.toString(layout.getX(v)));
        graphWriter.addVertexData("y", null, "0", (v) -> Double.toString(layout.getY(v)));
        graphWriter.addVertexData("v_prefix", null, "V", (v) -> v.getPrefix());
        graphWriter.addVertexData("v_index", null, "0", (v) -> Integer.toString(v.getIndex()));
        graphWriter.addVertexData("v_weight", null, "0.0", (v) -> Integer.toString(v.getWeight()));

        graphWriter.addEdgeData("e_weight", null, "0.0", (e) -> Double.toString(e.getWeight()));
        graphWriter.addEdgeData("e_prefix", null, "E", (e) -> e.getPrefix());
        graphWriter.addEdgeData("e_index", null, "0", (e) -> Integer.toString(e.getIndex()));

        graphWriter.save(graph, out);

    }

    public static Graph<Vertex, Edge> readFromFile(File file) throws FileNotFoundException {

        BufferedReader fileReader = new BufferedReader(new FileReader(file));

        /* Create the Graph Transformer */
        Transformer<GraphMetadata, Graph<Vertex, Edge>> graphTransformer = metadata ->
                metadata.getEdgeDefault().equals(metadata.getEdgeDefault().DIRECTED) ?
                        new DirectedSparseMultigraph<>() : new SparseMultigraph<>();

        /* Create the Vertex Transformer */
        Transformer<NodeMetadata, Vertex> vertexTransformer = metadata -> {
            Vertex v = new Vertex(metadata.getProperty("v_prefix"),
                            Integer.parseInt(metadata.getProperty("v_index")),
                            Integer.parseInt(metadata.getProperty("v_weight")));
            v.setX(Double.parseDouble(metadata.getProperty("x")));
            v.setY(Double.parseDouble(metadata.getProperty("y")));

            return v;
        };

        /* Create the Edge Transformer */
        Transformer<EdgeMetadata, Edge> edgeTransformer = metadata -> {
            Edge e = new Edge(metadata.getProperty("e_prefix"),
                    Integer.parseInt(metadata.getProperty("e_index")),
                    Double.parseDouble(metadata.getProperty("e_weight")));
            return e;
        };


        /* Create the Hyperedge Transformer */
        Transformer<HyperEdgeMetadata, Edge> hyperEdgeTransformer = metadata -> {
            Edge e = new Edge(metadata.getProperty("e_prefix"),
                    Integer.parseInt(metadata.getProperty("e_index")),
                    Double.parseDouble(metadata.getProperty("e_weight")));
            return e;
        };


        /* Create the graphMLReader2 */
        GraphMLReader2<Graph<Vertex, Edge>, Vertex, Edge> graphReader = new
                GraphMLReader2<Graph<Vertex, Edge>, Vertex, Edge>
                (fileReader, graphTransformer, vertexTransformer,
                        edgeTransformer, hyperEdgeTransformer);

        Graph<Vertex, Edge> graph = null;
        try {
            graph = graphReader.readGraph();
        } catch (GraphIOException ex) {
            System.out.println("reading graph error");
            ex.printStackTrace();
        }
        return graph;
    }
}
