package app.libmgmt.initialize;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

public class AdminInitializer extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Logger.getLogger("javafx").setLevel(java.util.logging.Level.SEVERE);
        FXMLLoader fxmlLoader = new FXMLLoader(AdminInitializer.class.getResource(
                "/fxml/standby-screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Welcome to Library Management System");
        System.out.println(stage);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}