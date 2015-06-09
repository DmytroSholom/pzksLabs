package edu.sholom.pzks.lab.controller;

import edu.sholom.pzks.lab.model.AutoGraphParams;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.function.Consumer;

/**
 * Created by Dmytro on 31.05.2015.
 */
public class AutoGraphParamsContoller {

    @FXML
    private TextField numOfVertices;

    @FXML
    private TextField graphCorrelation;

    @FXML
    private TextField vertexMinWeight;

    @FXML
    private TextField vertexMaxWeight;

    private Consumer<AutoGraphParams> autoGraphManager;

    public void setAutoGraphManager(Consumer<AutoGraphParams> autoGraphManager){
        this.autoGraphManager = autoGraphManager;
    }

    @FXML
    private void cancel(){
        ((Stage)numOfVertices.getScene().getWindow()).close();
    }

    @FXML
    private void process(){
        AutoGraphParams autoGraphParams = new AutoGraphParams();
        try {
            autoGraphParams.setNumOfVertices(Integer.parseInt(numOfVertices.getText()));
            autoGraphParams.setVertixMaxWeight(Integer.parseInt(vertexMaxWeight.getText()));
            autoGraphParams.setVertixMinWeight(Integer.parseInt(vertexMinWeight.getText()));
            autoGraphParams.setCorrelation(Double.parseDouble(graphCorrelation.getText()));
            autoGraphManager.accept(autoGraphParams);
            ((Stage)numOfVertices.getScene().getWindow()).close();
        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Wrong input");
            alert.setHeaderText("Some of input fields was filled incorrectly");
            alert.setContentText("You should use only integer numbers for all fields(correlation can be float)");
            alert.show();
            e.printStackTrace();
        }
    }


}
