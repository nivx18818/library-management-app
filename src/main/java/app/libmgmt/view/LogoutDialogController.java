package app.libmgmt.view;

import animatefx.animation.ZoomOut;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;
import app.libmgmt.view.admin.AdminGlobalController;

import java.io.IOException;

public class LogoutDialogController {

    @FXML
    private JFXButton closeDialogButton;

    @FXML
    private Pane closePane;

    @FXML
    private Pane container;

    @FXML
    private ImageView imgClose;

    @FXML
    private JFXButton confirmButton;

    @FXML
    private Pane confirmPane;

    @FXML
    private Label confirmLabel;

    public void initialize() {
        System.out.println("Logout Dialog Controller initialized");
        AnimationUtils.hoverCloseIcons(closeDialogButton, imgClose);
    }

    @FXML
    void closeButtonOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
    }

    @FXML
    void confirmButtonOnAction(ActionEvent event) throws IOException {
        ChangeScene.closePopUp();
        backToLoginForm();
    }

    @FXML
    void confirmButtonOnMouseEntered() {
        confirmPane.setStyle("-fx-background-color: #d7d7d7; -fx-background-radius: 10");
        confirmLabel.setStyle("-fx-text-fill: #000000");
    }

    @FXML
    void confirmButtonOnMouseExited() {
        confirmPane.setStyle("-fx-background-color: #000; -fx-background-radius: 10");
        confirmLabel.setStyle("-fx-text-fill: #fff");
    }

    public void backToLoginForm() throws IOException {
        ZoomOut zo = new ZoomOut(AdminGlobalController.getInstance().getGlobalFormContainer());

        ChangeScene.navigateToScene("loading-form.fxml");

        zo.setOnFinished(event -> {
            try {
                ChangeScene.changeInterfaceWindow((Stage) LoadingPageController.getInstance().getLoadingPane().getScene().getWindow(),
                        "/fxml/login-form.fxml", "Login Window");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        zo.play();
    }
}
