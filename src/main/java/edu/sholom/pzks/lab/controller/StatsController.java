package edu.sholom.pzks.lab.controller;

import edu.sholom.pzks.lab.algorithm.scheduler.utils.CountedVertex;
import edu.sholom.pzks.lab.model.Vertex;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

/**
 * Created by Dmytro on 03.06.2015.
 */
public class StatsController {

    @FXML
    private TableView<VertexStats> statsTable;

    @FXML
    private TableColumn<VertexStats, String> Id;

    @FXML
    private TableColumn<VertexStats, Integer> Tstart;

    @FXML
    private TableColumn<VertexStats, Integer> Tend;

    @FXML
    private TableColumn<VertexStats, Integer> Nstart;

    @FXML
    private TableColumn<VertexStats, Integer> Nend;

    private List<CountedVertex> stats;

    private final ObservableList<VertexStats> data =
            FXCollections.observableArrayList();

    @FXML
    private void initialize(){
        Id.setCellValueFactory(new PropertyValueFactory<VertexStats, String>("id"));
        Tstart.setCellValueFactory(new PropertyValueFactory<VertexStats, Integer>("Tstart"));
        Tend.setCellValueFactory(new PropertyValueFactory<VertexStats, Integer>("Tend"));
        Nstart.setCellValueFactory(new PropertyValueFactory<VertexStats, Integer>("Nstart"));
        Nend.setCellValueFactory(new PropertyValueFactory<VertexStats, Integer>("Nend"));

        statsTable.setItems(data);
    }

    public List<CountedVertex> getStats() {
        return stats;
    }

    public void setStats(List<CountedVertex> stats) {
        this.stats = stats;
        stats.forEach(this::addToData);

    }

    private void addToData(CountedVertex vertex){
        data.add(new VertexStats(vertex.getVertex().getPrefix()+vertex.getVertex().getIndex(), vertex.getMaxWToStart(), vertex.getMaxWToEnd(),
                vertex.getMaxNToStart(), vertex.getMaxNToEnd()));
    }

    public static class VertexStats{
        private final SimpleStringProperty id;
        private final SimpleIntegerProperty Tstart;
        private final SimpleIntegerProperty Tend;
        private final SimpleIntegerProperty Nstart;
        private final SimpleIntegerProperty Nend;

        private VertexStats(String id, int Tstart, int Tend, int Nstart, int Nend){
            this.id = new SimpleStringProperty(id);
            this.Tstart = new SimpleIntegerProperty(Tstart);
            this.Tend = new SimpleIntegerProperty(Tend);
            this.Nstart = new SimpleIntegerProperty(Nstart);
            this.Nend = new SimpleIntegerProperty(Nend);
        }


        public String getId() {
            return id.get();
        }

        public SimpleStringProperty idProperty() {
            return id;
        }

        public void setId(String id) {
            this.id.set(id);
        }

        public int getTstart() {
            return Tstart.get();
        }

        public SimpleIntegerProperty tstartProperty() {
            return Tstart;
        }

        public void setTstart(int tstart) {
            this.Tstart.set(tstart);
        }

        public int getTend() {
            return Tend.get();
        }

        public SimpleIntegerProperty tendProperty() {
            return Tend;
        }

        public void setTend(int tend) {
            this.Tend.set(tend);
        }

        public int getNstart() {
            return Nstart.get();
        }

        public SimpleIntegerProperty nstartProperty() {
            return Nstart;
        }

        public void setNstart(int nstart) {
            this.Nstart.set(nstart);
        }

        public int getNend() {
            return Nend.get();
        }

        public SimpleIntegerProperty nendProperty() {
            return Nend;
        }

        public void setNend(int nend) {
            this.Nend.set(nend);
        }
    }
}
