package view;

import animatefx.animation.ZoomOut;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.AnimationUtils;
import util.ChangeScene;

import java.io.IOException;
import java.util.logging.Logger;

public class StandbyScreenController {

    @FXML
    private Pane logoPane;

    @FXML
    private Pane standbyScreenContainer;

    @FXML
    public void initialize() {
        Logger.getLogger("javafx").setLevel(java.util.logging.Level.SEVERE);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(4), e -> {

                ZoomOut zoomOut = new ZoomOut(logoPane);

                zoomOut.setOnFinished(event -> {
                    try {
                        ChangeScene.changeInterfaceWindow((Stage) standbyScreenContainer.getScene().getWindow(),
                                "/fxml/login-form.fxml", "Login Window");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                zoomOut.play();
        }));
        timeline.play();
    }

}
