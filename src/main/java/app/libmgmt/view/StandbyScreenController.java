package app.libmgmt.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import app.libmgmt.util.ChangeScene;

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

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2.5), e -> {
            try {
                ChangeScene.changeInterfaceWindow((Stage) standbyScreenContainer.getScene().getWindow(),
                        "/fxml/login-form.fxml", "BookWorm");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }));
        timeline.play();
    }

}
