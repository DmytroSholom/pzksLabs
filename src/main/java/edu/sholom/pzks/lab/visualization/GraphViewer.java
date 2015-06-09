package edu.sholom.pzks.lab.visualization;

import edu.sholom.pzks.lab.model.Edge;
import edu.sholom.pzks.lab.model.Vertex;
import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import javafx.application.Platform;
import javafx.scene.control.TextInputDialog;
import org.apache.commons.collections15.Factory;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by Dmytro on 21.05.2015.
 */
public class GraphViewer {

    private Graph<Vertex, Edge> graph;
    private int nodeCount, edgeCount;
    private Factory<Vertex> vertexFactory;
    private Factory<Edge> edgeFactory;
    private VisualizationViewer<Vertex, Edge> editableGraphViewer;
    private AbstractLayout<Vertex, Edge> layout;
    private EditingModalGraphMouse gm;

    public GraphViewer(boolean directed) {
        // Graph<V, E> where V is the type of the vertices and E is the type of the edges
        if(directed) {
            graph = new DirectedSparseMultigraph<>();
        } else {
            graph = new SparseMultigraph<>();
        }
        nodeCount = 0;
        edgeCount = 0;

        vertexFactory = () -> {
            int weight = promptWeightFromUser("Specify the vertex weight, please...");
            String prefix = directed ? "T" : "CS";
            return new Vertex(prefix, nodeCount++, weight);
        };
        edgeFactory = () -> {
            double weight = promptWeightFromUser("Specify the edge weight, please...");
            return new Edge("L", edgeCount++, weight);
        };
    }

    public VisualizationViewer<Vertex, Edge> drawGeneratedGraph(Graph<Vertex, Edge> graph){
        this.layout = new CircleLayout<>(graph);
        this.graph = graph;
        return renderGraphCanvas();
    }

    public VisualizationViewer<Vertex, Edge> drawEditableGraph(Graph<Vertex, Edge> graph){
        StaticLayout<Vertex, Edge> layout = new StaticLayout<>(graph, v -> new Point2D.Double(v.getX(), v.getY()));
        this.layout = layout;
        this.graph = graph;
        return renderGraphCanvas();
    }

    /**
     * create editable graph visualization viewer with static layout
     * @return component with editable graph
     */
    public VisualizationViewer<Vertex, Edge> createGraphEditableViewer(){
        StaticLayout<Vertex, Edge> layout = new StaticLayout<>(this.graph);
        this.layout = layout;
        return renderGraphCanvas();
    }

    private VisualizationViewer<Vertex, Edge> renderGraphCanvas(){
        VisualizationViewer<Vertex, Edge> vv = new VisualizationViewer<>(this.layout);
        // Show vertex and edge labels
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        // Create a graph mouse and add it to the visualization viewer
        // Our Vertices are going to be Integer objects so we need an Integer factory
        EditingModalGraphMouse gm = new EditingModalGraphMouse(vv.getRenderContext(),
                this.vertexFactory, this.edgeFactory);
        vv.setGraphMouse(gm);
        gm.setMode(ModalGraphMouse.Mode.EDITING); // Start off in editing mode
        this.editableGraphViewer = vv; //save reference to viewer
        this.gm = gm;
        return vv;
    }

    public void setMouseMode(EditingMode mode){
        switch (mode){
            case EDITING_MODE:
                gm.setMode(ModalGraphMouse.Mode.EDITING);
                break;
            case PICKING_MODE:
                gm.setMode(ModalGraphMouse.Mode.PICKING);
                break;
            case TRANSFORMING_MODE:
                gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
                break;
            default:
                break;
        }
    }

    public VisualizationViewer<Vertex, Edge> getGraphEditableViewer(){
        return this.editableGraphViewer;
    }

    public AbstractLayout<Vertex, Edge> getLayout(){
        return this.layout;
    }

    public Factory<Vertex> getVertexFactory(){
        return this.vertexFactory;
    }

    public Factory<Edge> getEdgeFactory(){
        return this.edgeFactory;
    }

    /**
     * places vertices on a circle
     * @return swing component with circle layout
     */
    public BasicVisualizationServer<Vertex,Edge> getCircleLayout(){
        Layout<Vertex, Edge> layout = new CircleLayout<>(this.graph);
        BasicVisualizationServer<Vertex,Edge> vv = new BasicVisualizationServer<>(layout);

        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        return vv;
    }

    /**
     * places vertices with a self-organizing map layout
     * @return swing component with isom layout
     */
    public BasicVisualizationServer<Vertex,Edge> getISOMLayout(){
        Layout<Vertex, Edge> layout = new ISOMLayout<>(this.graph);
        BasicVisualizationServer<Vertex,Edge> vv = new BasicVisualizationServer<>(layout);

        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        return vv;
    }

    /**
     * places vertices with a Fruchterman-Reingold algorithm (force-directed)
     * @return swing component with FR layout
     */
    public BasicVisualizationServer<Vertex,Edge> getFRLayout(){
        Layout<Vertex, Edge> layout = new FRLayout<>(this.graph);
        BasicVisualizationServer<Vertex,Edge> vv = new BasicVisualizationServer<>(layout);

        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());

        return vv;
    }

    public Graph<Vertex, Edge> getGraph(){
        return this.graph;
    }

    private int promptWeightFromUser(String message){
        FutureTask<String> query = new FutureTask<String>(() -> {
            TextInputDialog dialog = new TextInputDialog("0");
            dialog.setTitle("Weight setting");
            dialog.setHeaderText(message);
            dialog.setContentText("Weight:");

            Optional<String> result = dialog.showAndWait();
            if(result.isPresent()) {
                return result.get();
            } else {
                return null;
            }
        });
        Platform.runLater(query);
        String weightStr = null;
        try {
            weightStr = query.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("User prompt exception...");
            e.printStackTrace();
        }
        //String weightStr =  JOptionPane.showInputDialog(message, null);
        try{
            return Integer.parseInt(weightStr);
        } catch (NumberFormatException ex){
            System.out.println("Weight parsing exception");
            return promptWeightFromUser("Wrong input! " + message);
        }
    }


}
