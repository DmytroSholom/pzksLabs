package edu.sholom.pzks.lab.controller;

import com.sun.org.glassfish.external.statistics.Stats;
import edu.sholom.pzks.lab.algorithm.GraphGenerator;
import edu.sholom.pzks.lab.algorithm.scheduler.Scheduler;
import edu.sholom.pzks.lab.algorithm.scheduler.utils.CountedVertex;
import edu.sholom.pzks.lab.algorithm.scheduler.utils.CriticalPathCounter;
import edu.sholom.pzks.lab.model.Edge;
import edu.sholom.pzks.lab.model.Vertex;
import edu.sholom.pzks.lab.service.AlgorithmProvider;
import edu.sholom.pzks.lab.service.GraphIO;
import edu.sholom.pzks.lab.visualization.EditingMode;
import edu.sholom.pzks.lab.visualization.GraphViewer;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import edu.uci.ics.jung.graph.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Dmytro on 20.05.2015.
 */
public class AppController {

    private GraphViewer taskGraphViewer;
    private GraphViewer systemGraphViewer;
    private GraphViewer currentGraphViewer;
    private SwingNode currentGraphVisualiser;
    private boolean directed ;

    @FXML
    private TabPane mainTabPane;

    @FXML
    private SwingNode taskGraphVisualizer;

    @FXML
    private SwingNode systemGraphVisualizer;

    @FXML
    private ToggleGroup layoutGroup;

    @FXML
    private Button generateBtn;

    @FXML
    private Button statsBtn;

    @FXML
    private Button taskOneBtn;

    @FXML
    private Button taskTwelveBtn;

    @FXML
    private Button taskSixteenBtn;

    @FXML
    public void initialize(){
        //task graph tab selected by default
        currentGraphVisualiser = taskGraphVisualizer;
        directed = true;
        //add listener to track tab changes and currently displayed graph
        mainTabPane.getSelectionModel().selectedIndexProperty().addListener(
                (o, i1, i2) -> {
                    if(i2.intValue() - i1.intValue()>0){
                        currentGraphVisualiser = systemGraphVisualizer;
                        currentGraphViewer = systemGraphViewer;
                        directed = false;
                        //disable btns
                        generateBtn.setDisable(true);
                        taskOneBtn.setDisable(true);
                        taskTwelveBtn.setDisable(true);
                        taskSixteenBtn.setDisable(true);
                        statsBtn.setDisable(true);

                    } else{
                        currentGraphVisualiser = taskGraphVisualizer;
                        currentGraphViewer = taskGraphViewer;
                        directed = true;
                        //enable
                        generateBtn.setDisable(false);
                        taskOneBtn.setDisable(false);
                        taskTwelveBtn.setDisable(false);
                        taskSixteenBtn.setDisable(false);
                        statsBtn.setDisable(false);
                    }
                }
        );
    }

    @FXML
    private void create(){
        createViewer();
        currentGraphVisualiser.setContent(currentGraphViewer.createGraphEditableViewer());
    }

    @FXML
    private void open(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open saved graph");
        File file = fileChooser.showOpenDialog(mainTabPane.getScene().getWindow());
        if(file == null){
            System.out.println("Error while opening file");
        }
        try {
            Graph<Vertex, Edge> graph = GraphIO.readFromFile(file);
            if(currentGraphViewer == null){
                createViewer();
            }
            currentGraphVisualiser.setContent(currentGraphViewer.drawEditableGraph(graph));
        } catch (Exception ex){
            System.out.println("Error reading file");
            ex.printStackTrace();
        }
    }

    @FXML
    private void save(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save graph");
        File file = fileChooser.showSaveDialog(mainTabPane.getScene().getWindow());

        try {
            GraphIO.writeToFile(file, currentGraphViewer.getGraph(), currentGraphViewer.getLayout());
        }  catch (IOException e) {
            System.out.println("Error writing to file");
            e.printStackTrace();
        }
    }

    @FXML
    private void editingMode(){
        currentGraphViewer.setMouseMode(EditingMode.EDITING_MODE);
    }

    @FXML
    private void pickingMode(){
        currentGraphViewer.setMouseMode(EditingMode.PICKING_MODE);
    }

    @FXML
    private void transformingMode(){
        currentGraphViewer.setMouseMode(EditingMode.TRANSFORMING_MODE);
    }

    @FXML
    private void staticLayout(){
        currentGraphVisualiser.setContent(currentGraphViewer.getGraphEditableViewer());
    }

    @FXML
    private void circleLayout(){
        currentGraphVisualiser.setContent(currentGraphViewer.getCircleLayout());
    }

    @FXML
    private void ISOMLayout(){
        currentGraphVisualiser.setContent(currentGraphViewer.getISOMLayout());
    }

    @FXML
    private void FRLayout(){
        currentGraphVisualiser.setContent(currentGraphViewer.getFRLayout());
    }

    @FXML
    private void checkGraph(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Graph checker");
        String message;
        if (currentGraphViewer == null || currentGraphViewer.getGraph().getVertices().size()==0){
            alert.setHeaderText("There in no graphs to check");
            message = "Please create graph first";
        } else if(directed){
            alert.setHeaderText("Checking for cycles in tasks graph");
            message = AlgorithmProvider.checkGraphForCycles(currentGraphViewer.getGraph()) ? "Graph has cycles" : "Graph has no cycles";
        } else {
            alert.setHeaderText("Checking for connectivity in computer system graph");
            message = AlgorithmProvider.checkGraphConnectivity(currentGraphViewer.getGraph()) ?
                    "Graph is connected" : "Graph isn`t connected";
        }
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    private void generateGraph() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/auto_graph_params_view.fxml"));

        GridPane autoGraphParamsView = fxmlLoader.load();

        //handler for graph generating and drawing
        ((AutoGraphParamsContoller)fxmlLoader.getController()).setAutoGraphManager(autoGraphParams -> {
            //generate graph

            GraphGenerator generator = new GraphGenerator();
            Graph<Vertex, Edge> graph = generator.generate(autoGraphParams);

            System.out.println("Graph was succesfully generated");

            CriticalPathCounter counter = new CriticalPathCounter(graph);
            counter.countForwardPathLengthsByWeight().countBackwardPathLengthsByAmount()
                    .countBackwardPathLengthsbyWeigth().countForwardPathLengthsByAmount()
                    .getcountedVertices();



            //displayGraph
            if(currentGraphViewer == null){
                createViewer();
            }
            currentGraphVisualiser.setContent(currentGraphViewer.drawGeneratedGraph(graph));
        });

        Scene scene = new Scene(autoGraphParamsView);
        Stage stage = new Stage();
        stage.setTitle("Graph parameters settings");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void stats() throws IOException{
        if(!graphExists()){
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/stats.fxml"));

        TableView statsView = fxmlLoader.load();

        CriticalPathCounter counter = new CriticalPathCounter(currentGraphViewer.getGraph());
        ((StatsController)fxmlLoader.getController()).setStats(counter.countForwardPathLengthsByWeight()
                .countBackwardPathLengthsByAmount().countBackwardPathLengthsbyWeigth()
                .countForwardPathLengthsByAmount().getcountedVertices());

        Scene scene = new Scene(statsView);
        Stage stage = new Stage();
        stage.setTitle("Graph stats");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void taskOne() throws IOException{
        if(!graphExists()){
            return;
        }

        showScheduler(graph -> {
            CriticalPathCounter counter = new CriticalPathCounter(graph);
            List<CountedVertex> buffer = counter.countBackwardPathLengthsbyWeigth().countBackwardPathLengthsByAmount().getcountedVertices();
            int maxW = 0, maxN = 0;
            for(CountedVertex vertex: buffer){
                maxW = vertex.getMaxWToEnd() > maxW ? vertex.getMaxWToEnd() : maxW;
                maxN = vertex.getMaxNToEnd() > maxN ? vertex.getMaxNToEnd() : maxN;
            }
            System.out.println("maxW:"+maxW+ " " + "maxN"+maxN);
            List<Scheduler.ValuedVertex> result = new ArrayList<>();
            for(CountedVertex vertex: buffer){
                Scheduler.ValuedVertex valued = new Scheduler.ValuedVertex(vertex.getVertex());
                double value = ((double)vertex.getMaxNToEnd()) / maxN + ((double)vertex.getMaxWToEnd()) / maxW;
                valued.setValue(value);
                result.add(valued);
            }
            result.sort(Scheduler.ValuedVertex::compareByValueDesc);
            System.out.println(result);
            return result;
        });
    }

    @FXML
    private void taskTwelve() throws IOException{
        if(!graphExists()){
            return;
        }
        showScheduler(graph -> {
            List<Scheduler.ValuedVertex> result = new ArrayList<>();
            graph.getVertices().forEach(vertex -> {
                Scheduler.ValuedVertex valued = new Scheduler.ValuedVertex(vertex);
                valued.setValue(graph.getOutEdges(vertex).size());
                result.add(valued);
            });
            result.sort(Scheduler.ValuedVertex::compareByValueDesc);
            return result;

        });

    }

    @FXML
    private void taskSixteen() throws IOException{
        if(!graphExists()){
            return;
        }

        showScheduler(graph -> {
            List<Scheduler.ValuedVertex> result = new ArrayList<>();
            CriticalPathCounter counter = new CriticalPathCounter(graph);
            counter.countForwardPathLengthsByWeight().getcountedVertices().forEach(countedVertex -> {
                Scheduler.ValuedVertex valued = new Scheduler.ValuedVertex(countedVertex.getVertex());
                valued.setValue(countedVertex.getMaxWToStart());
                result.add(valued);
            });
            result.sort(Scheduler.ValuedVertex::compareByValueAsc);
            return result;
        });
    }

    private void showScheduler(Scheduler scheduler) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/scheduled_view.fxml"));

        TableView statsView = fxmlLoader.load();

        ((SchedulerController)fxmlLoader.getController()).setGraph(currentGraphViewer.getGraph());
        ((SchedulerController)fxmlLoader.getController()).setScheduler(scheduler);

        Scene scene = new Scene(statsView);
        Stage stage = new Stage();
        stage.setTitle("Scheduler");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private boolean graphExists(){
        if (currentGraphViewer == null || currentGraphViewer.getGraph().getVertices().size()==0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Graph checker");
            alert.setHeaderText("There in no graphs to check");
            alert.setContentText("Please create graph first");
            alert.show();
            return false;
        }
        return true;
    }

    private void createViewer(){
        currentGraphViewer = new GraphViewer(directed);
        if(directed) {
            taskGraphViewer = currentGraphViewer;
        } else {
            systemGraphViewer = currentGraphViewer;
        }
    }


}
