package edu.sholom.pzks.lab.controller;

import edu.sholom.pzks.lab.algorithm.scheduler.Scheduler;
import edu.sholom.pzks.lab.model.Edge;
import edu.sholom.pzks.lab.model.Vertex;
import edu.uci.ics.jung.graph.Graph;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


/**
 * Created by Dmytro on 03.06.2015.
 */
public class SchedulerController {

    @FXML
    private TableView<ScheduledVertex> schedulingTable;

    @FXML
    private TableColumn<ScheduledVertex, Integer> sequenceNumber;

    @FXML
    private TableColumn<ScheduledVertex, String> taskId;

    @FXML
    private TableColumn<ScheduledVertex, Double> value;

    private int i;

    private Scheduler scheduler;

    private Graph<Vertex, Edge> graph;

    private final ObservableList<ScheduledVertex> data =
            FXCollections.observableArrayList();

    public Graph<Vertex, Edge> getGraph() {
        return graph;
    }

    public void setGraph(Graph<Vertex, Edge> graph) {
        this.graph = graph;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
        scheduler.run(this.graph).forEach(this::addToData);

    }

    private void addToData(Scheduler.ValuedVertex vertex){
        data.add(new ScheduledVertex(i++,
                vertex.getVertex().getPrefix() + vertex.getVertex().getIndex(), vertex.getValue()));
    }

    @FXML
    private void initialize(){
        i = 0;
        sequenceNumber.setCellValueFactory(new PropertyValueFactory<ScheduledVertex, Integer>("sequenceNumber"));
        taskId.setCellValueFactory(new PropertyValueFactory<ScheduledVertex, String>("taskId"));
        value.setCellValueFactory(new PropertyValueFactory<ScheduledVertex, Double>("value"));

        schedulingTable.setItems(data);
    }

    public static class ScheduledVertex{
        private final SimpleIntegerProperty sequenceNumber;
        private final SimpleStringProperty taskId;
        private final SimpleDoubleProperty value;


        private ScheduledVertex(int sequenceNumber, String taskId, double value){
            this.sequenceNumber = new SimpleIntegerProperty(sequenceNumber);
            this.taskId = new SimpleStringProperty(taskId);
            this.value = new SimpleDoubleProperty(value);
        }

        public String getTaskId() {
            return taskId.get();
        }

        public SimpleStringProperty taskIdProperty() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId.set(taskId);
        }

        public int getSequenceNumber() {
            return sequenceNumber.get();
        }

        public SimpleIntegerProperty sequenceNumberProperty() {
            return sequenceNumber;
        }

        public void setSequenceNumber(int sequenceNumber1) {
            this.sequenceNumber.set(sequenceNumber1);
        }

        public double getValue() {
            return value.get();
        }

        public SimpleDoubleProperty valueProperty() {
            return value;
        }

        public void setValue(double value) {
            this.value.set(value);
        }
    }
}
