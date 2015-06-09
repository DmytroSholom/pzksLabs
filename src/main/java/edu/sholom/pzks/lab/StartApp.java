package edu.sholom.pzks.lab;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Created by Dmytro on 21.05.2015.
 */
public class StartApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/lab_view.fxml"));
        AnchorPane appView = fxmlLoader.load();
        Scene scene = new Scene(appView);

        primaryStage.setTitle("Sholom Dmytro. IP-41m. PZKS labs.");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
